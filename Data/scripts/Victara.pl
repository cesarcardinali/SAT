my $TEMP = "file-list.txt";
my $VERSION = "v1.5";
my $OUTPUT = "report-output_".$VERSION.".txt";
my $DATE = "31-07-2013";
my $PATH = "c:\\Bug2Go-scripts\\";
my $year = 2014;
my $UTC_offset = -5;

my $BUGREPORT_PRE = "bugreport";
my $SUMMARY_PRE = "summary";
my $OUTPUT_PRE = "report-output";

use strict;
use warnings;

####################################################################
#  Control parameters for optional functionality                   #
####################################################################
my $PARSING_PARAMETERS = "c:/bug2go-scripts/report_setup.xml";
my $doSystem =  parseXmlLine($PARSING_PARAMETERS,"parse_system_log");
my $doMain = parseXmlLine($PARSING_PARAMETERS,"parse_main_log");
my $doKernel = parseXmlLine($PARSING_PARAMETERS,"parse_kernel_log");
my $doPmLog = parseXmlLine($PARSING_PARAMETERS,"parse_pm_log");
my $doBtd = parseXmlLine($PARSING_PARAMETERS,"parse_BTD_file");
my $doBugReport = parseXmlLine($PARSING_PARAMETERS,"parse_bugreport");
my $doEvents = parseXmlLine($PARSING_PARAMETERS,"parse_events_log");
my $doRadio = parseXmlLine($PARSING_PARAMETERS,"parse_radio_log");

my $batt_cap = 2300;

my %files;
my %fnames;
my $events_files = "";
my $kernel_files = "";
my $main_files = "";
my $radio_files = "";
my $system_files = "";
my $MAIN = "main";
my $KERNEL = "kernel";
my $SYSTEM = "system";
my $RADIO = "radio";
my $EVENTS = "events";

$fnames{$MAIN} = "";
$fnames{$KERNEL} = "";
$fnames{$SYSTEM} = "";
$fnames{$RADIO} = "";
$fnames{$EVENTS} = "";
$fnames{"end"} = "";
$files{$MAIN} = "";
$files{$KERNEL} = "";
$files{$SYSTEM} = "";
$files{$RADIO} = "";
$files{$EVENTS} = "";
$files{"end"} = "";


#for build-report.pl  add the following to the top after $OUTPUT is declared:
my $num_args = $#ARGV + 1;
if($num_args >= 1)
{
    $OUTPUT = $ARGV[0]."_".$OUTPUT;
}
if($num_args == 2)
{
    $PATH = $ARGV[1];
}
if ($num_args == 3)
{
    $PATH = $ARGV[1];
    $batt_cap = $ARGV[2];
}

system("dir > $TEMP");

open(FILE1, "$TEMP") or die "Unable to Open $TEMP\n" ;
while (<FILE1>)
{
  if (($_ =~ /$MAIN/ && $doMain eq "TRUE") || ($_ =~ /$SYSTEM/ && $doSystem eq "TRUE") ||
      ($_ =~ /$EVENTS/ && $doEvents eq "TRUE") || ($_ =~ /$RADIO/ && $doRadio eq "TRUE") ||
      ($_ =~ /$KERNEL/ && $doKernel eq "TRUE")) 
  {
    if ($_ =~ /.*\s(\S*[\.|_\-])(\S*)(\.txt$)/)
    {
#        print "$1$2$3\n";
        my $key = $2;
        my $fname = "$1$2$3";
        if ($1 =~ /\.\d+/)
        {
            $files{$key} = "$fname"."+"."$files{$key}";
            $fnames{$key} = "full\.$key\.txt";
        }
        else
        {
            $files{$key} .= "$fname";
            if ($fnames{$key} eq "")
            {
                $fnames{$key} = "$fname";
            }
        }
    }
    elsif ($_ =~ /.*\s(BT\d\_\S*\_)(\S*)(\.log$)/)
    {
        my $key = $2;
        my $fname = "$1$2$3";
        if ($2 =~ /\.\d+/)
        {
            $key = "main";
        }
        $fnames{$key} = $fname;
        #print "Key: $key, Filename: $fname\n";
    }
  }
}
close(FILE1);
system("del $TEMP");

foreach my $flist (keys %files)
{
#    print "$flist $files{$flist}\n";
#    print "$fnames{$flist}\n";
    if ($files{$flist} =~ /(\S*)\+(\S*)$/)
    {
        my $dfile = $1;
        system("copy $1\+$2 full\.$flist\.txt");
        #print("copy $1\+$2 full\.$flist\.txt\n");
        system("del $2");
        #print("del $2\n");
        while ($dfile ne "")
        {
            if ($dfile =~ /(\S*)\+(\S*)$/)
            {
                system("del $2");
                #print("del $2\n");
                $dfile = $1;
            }
            else
            {
                system("del $dfile");
                #print("del $dfile\n");
                $dfile = "";
            }
        }
    }
}
#printf "Filename: %s\n", $fnames{"main"};

open(FILE2, ">$OUTPUT");
print FILE2 "\<ReportBlockStart\>$VERSION\<\/ReportBlockStart\>\n";
print FILE2 "\<ReportVersionDate\>$DATE\<\/ReportVersionDate\>\n\n";
close(FILE2);

my $f = $fnames{"kernel"};
if($doKernel eq "TRUE")
{
	if ($f ne "")
	{
		system("perl $PATH\/parse-kernel.pl $f >> $OUTPUT");
		if($doPmLog eq "TRUE")
		{
			system("python $PATH\/pm_log_parser.py -p MSM $f >> $OUTPUT");
		#	system("perl $PATH\/pm_log_parser.pl $f >> $OUTPUT");
		}
	}
	else
	{
		open(FILE2, ">>$OUTPUT");
		print FILE2 "\n*** KERNEL log not available\n";
		close(FILE2);
	}
}

$f = $fnames{"main"};
my $main_log = $fnames{"main"};
if($doMain eq "TRUE")
{
	if ($f ne "")
	{
		open(FILE2, ">>$OUTPUT");
		print FILE2 "\n\nParsing main log for wake locks, conn state, and foreground applications\n";
		close(FILE2);
		system("perl $PATH\/parse-main.pl --year $year $f >> $OUTPUT");
	}
	else
	{
		open(FILE2, ">>$OUTPUT");
		print FILE2 "\n*** MAIN log not available\n";
		close(FILE2);
	}
}

my $events_log = $fnames{$EVENTS};
if($doEvents eq "TRUE")
{
	if ($events_log ne "")
	{
		open(FILE2, ">>$OUTPUT");
		print FILE2 "\n\nParsing events log for battery metering\n";
		print FILE2     "=======================================\n";
		print FILE2 "\<DataBlockStart\>Event log Parsing\<\/DataBlockStart\>\n";
		close(FILE2);
		system("perl $PATH\/parse-events.pl --year $year $events_log >> $OUTPUT");
		open(FILE2, ">>$OUTPUT");
		print FILE2 "\<DataBlockEnd\>Event log Parsing\<\/DataBlockEnd\>\n";
		close(FILE2);
	}
	else
	{
		open(FILE2, ">>$OUTPUT");
		print FILE2 "\n*** EVENTS log not available\n";
		close(FILE2);
	}
}

$f = $fnames{"system"};
if($doSystem eq "TRUE")
{
	if ($f ne "")
	{
		open(FILE2, ">>$OUTPUT");
		print FILE2 "\nParsing system log for wake ups and conn state\n";
		print FILE2   "==============================================\n";
		print FILE2 "\<DataBlockStart\>System log Parsing\<\/DataBlockStart\>\n";
		close(FILE2);
		system("perl $PATH\/parse-system.pl --year $year $f >> $OUTPUT");
		open(FILE2, ">>$OUTPUT");
		print FILE2 "\<DataBlockEnd\>System log Parsing\<\/DataBlockEnd\>\n";
		close(FILE2);
	}
	else
	{
		open(FILE2, ">>$OUTPUT");
		print FILE2 "\n*** EVENTS log not available\n";
		close(FILE2);
	}
}	

$f = $fnames{"radio"};
if($doRadio eq "TRUE")
{
	if ($f ne "")
	{
		open(FILE2, ">>$OUTPUT");
		print FILE2 "\nParsing radio log for RATs\n";
		print FILE2   "==========================\n";
		print FILE2 "\<DataBlockStart\>Radio log Parsing\<\/DataBlockStart\>\n";
		close(FILE2);
		system("perl $PATH\/parse-radio.pl $f >> $OUTPUT");
		open(FILE2, ">>$OUTPUT");
		print FILE2 "\<DataBlockEnd\>Radio log Parsing\<\/DataBlockEnd\>\n";
		close(FILE2);
	}
	else
	{
		open(FILE2, ">>$OUTPUT");
		print FILE2 "\n*** RADIO log not available\n";
		close(FILE2);
	}
}

if ($fnames{"end"} ne "")
{
    $f = $fnames{"end"};
}
else
{
    my $where = ".";
    my $name = "";
    if (-d "../bug2go") {
        $where = "../bug2go";
    }
    if (defined($name = <$where/bugreport*>)) {
        $f = $name;
    }
    elsif (defined($name = <$where/*bugreport*>)) {
        $f = $name;
    }
}

if($doBugReport eq "TRUE")
{
	if (-f $f)
	{
		open(FILE2, ">>$OUTPUT");
		print FILE2 "\nParsing Bugreport\n";
		print FILE2   "=================\n";
		print FILE2 "\<DataBlockStart\>Bugreport Parsing\<\/DataBlockStart\>\n";
		close(FILE2);
		system("perl $PATH\/parse-bugreport.pl $f $batt_cap >> $OUTPUT");

		open(FILE2, ">>$OUTPUT");
		print FILE2 "\<DataBlockEnd\>Bugreport Parsing\<\/DataBlockEnd\>\n";
		close(FILE2);
	}
}


if($doBtd eq "TRUE")
{
    system("perl $PATH\/parse_batt.pl >> $OUTPUT");
}
	
open(FILE2, ">>$OUTPUT");
print FILE2 "\<ReportBlockEnd\>$VERSION\<\/ReportBlockEnd\>\n";
close(FILE2);

sub parseXmlLine
{
	my $temp1 = "<".$_[1].">";
	my $temp2 = "<\/".$_[1].">";

	open(XML, "$_[0]") or die "Unable to open $_[0]\n";
	while (<XML>)
	{
		if($_ =~ m/$temp1(.*)$temp2/)
		{
			my $tempVal = $1;
			$tempVal =~ s/ //g;
			close(XML);
			return $tempVal;
		}
	}
	print "$_[1] not found in xml file.  Can't set parameter properly.\n";
	print "File must contain $temp1 SETTING $temp2\n";
	close(XML);
	die;
}

