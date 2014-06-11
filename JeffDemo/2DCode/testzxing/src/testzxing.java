import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.zxing.util.ZXingUtil;

public class testzxing {	
	
	/**
	 * @param args
	 */
	@SuppressWarnings({ "static-access"})
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// create Options object
		
		// create the command line parser
		CommandLineParser parser = new PosixParser();
				
		String dest = "";
		String url = "";
		String logo = "";
		
		
		// create the Options
		Options options = new Options();
		options.addOption( OptionBuilder.withLongOpt( "u" )
                .withDescription( "Url Address" )
                .hasArg()
                .withArgName("http://www.baidu.com")
                .create());
		
		options.addOption( OptionBuilder.withLongOpt( "o" )
                .withDescription( "Output File's Path [optional]" )
                .hasArg()
                .withArgName("result.png")
                .create());
		
		options.addOption( OptionBuilder.withLongOpt( "l" )
                .withDescription( "Logo's Path [optional]" )
                .hasArg()
               .withArgName("logo.png")
                .create());
		
		try {
			
			// parse the command line arguments
		    CommandLine line = parser.parse( options, args );
			
			if(!line.hasOption( "u" )) {
		        // print the value of block-size
				HelpFormatter formatter = new HelpFormatter();  
				formatter.printHelp( "2dcode -u url [-o result.png] [-l logo.png]", options ); 
				System.exit(1);
		    }
			
			dest = line.getOptionValue("o", "./result.png");
			url = line.getOptionValue("u");
			logo = line.getOptionValue("l", "");
			
			ZXingUtil.encodeQRCodeImage(url, null, dest, 300,
						300, logo);
			
			System.out.print("Output File["+ dest +"] is Successed.");

		} catch (Exception e) {
			HelpFormatter formatter = new HelpFormatter();  
			formatter.printHelp( "2dcode -u url [-o result.png] [-l logo.png]", options ); 
			e.printStackTrace();
		}
	}

}
