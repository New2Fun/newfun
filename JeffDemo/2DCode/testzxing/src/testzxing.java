import java.util.*;


import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.zxing.util.ZXingUtil;

public class testzxing {

	private static void help()
	{
		System.out.print("Example:java -jar 2dcode.jar -o result.png -u http://www.baidu.com -l logo.png\n");
	}
	
	
	/**
	 * @param args
	 */
	@SuppressWarnings({ "static-access"})
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// create Options object
		
		// create the command line parser
		CommandLineParser parser = new BasicParser();
				
		String dest = "";
		String url = "";
		String logo = "";
		
		
		// create the Options
		Options options = new Options();
		options.addOption( OptionBuilder.withLongOpt( "u" )
                .withDescription( "Url Address" )
                .hasArg()
                .withArgName("http://")
                .create());
		
		options.addOption( OptionBuilder.withLongOpt( "o" )
                .withDescription( "Output File's Path" )
                .hasArg()
                .withArgName("result.png")
                .create());
		
		options.addOption( OptionBuilder.withLongOpt( "l" )
                .withDescription( "Logo's Path" )
                .hasArg()
                .withArgName("logo.png")
                .create());
		
		try {
			
			// parse the command line arguments
		    CommandLine line = parser.parse( options, args );
			
			if(!line.hasOption( "u" ) || !line.hasOption( "o" )) {
		        // print the value of block-size
				help();
				System.exit(1);
		    }
			
			dest = line.getOptionValue("o");
			url = line.getOptionValue("u");
			logo = line.getOptionValue("l", "");
			
			ZXingUtil.encodeQRCodeImage(url, null, dest, 300,
						300, logo);

		} catch (Exception e) {
			help();
			e.printStackTrace();
		}
	}

}
