package com.nacid.report.export;

import com.nacid.data.DataConverter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.base.JRBoxPen;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.fonts.FontFamily;
import net.sf.jasperreports.engine.fonts.FontInfo;
import net.sf.jasperreports.engine.type.*;
import net.sf.jasperreports.engine.util.FileBufferedWriter;
import net.sf.jasperreports.engine.util.JRFontUtil;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRStyledText;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import java.awt.*;
import java.awt.font.TextAttribute;
import java.io.*;
import java.text.AttributedCharacterIterator;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Exports a JasperReports document to RTF format. It has binary output type and exports the document to
 * a free-form layout. It uses the RTF Specification 1.6 (compatible with MS Word 6.0, 2003 and XP).
 * 
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id: JRRtfExporter.java 3792 2010-05-12 14:23:56Z teodord $
 */
public class JRRtfExporterCustom extends JRAbstractExporter
{
	private static final Log log = LogFactory.getLog(JRRtfExporterCustom.class);
	
	private static final String RTF_EXPORTER_PROPERTIES_PREFIX = JRProperties.PROPERTY_PREFIX + "export.rtf.";

	/**
	 * The exporter key, as used in
	 * {@link GenericElementHandlerEnviroment#getHandler(net.sf.jasperreports.engine.JRGenericElementType, String)}.
	 */
	public static final String RTF_EXPORTER_KEY = JRProperties.PROPERTY_PREFIX + "rtf";
	
	/**
	 *
	 */
	protected static final String JR_PAGE_ANCHOR_PREFIX = "JR_PAGE_ANCHOR_";
	protected JRExportProgressMonitor progressMonitor = null;

	protected FileBufferedWriter colorWriter = null;
	protected FileBufferedWriter fontWriter = null;
	protected FileBufferedWriter writer = null;
	protected Writer rtfWriter = null;
	protected File destFile = null;

	protected int reportIndex = 0;

	protected List colors = null;
	protected List fonts = null;

	// z order of the graphical objects in .rtf file
	private int zorder = 1;

	/**
	 * @deprecated
	 */
	private Map fontMap = null;
	
	/**
	 * 
	 * @author bily 
	 *
	 */
//	private List<JRPrintElement> headerElements = new ArrayList<JRPrintElement>();
//	private List<JRPrintElement> footerElements = new ArrayList<JRPrintElement>();
//	private Map<Integer, JRPrintElement>  paragraphElements = new TreeMap<Integer, JRPrintElement>();
	private boolean            ignorePagination = false;
	private static final String IGNORE_PAGINATION_PROPERTY = "isIgnorePagination";
    private static Pattern CUSTOM_LIST_ITEM_PATTERN = Pattern.compile("^<cust-li>(.*?)</cust-li>$");
	protected class ExporterContext extends BaseExporterContext implements JRRtfExporterContext
	{
		public String getExportPropertiesPrefix()
		{
			return RTF_EXPORTER_PROPERTIES_PREFIX;
		}
	}
	
	protected JRRtfExporterContext exporterContext = new ExporterContext();

	
	/**
	 * Export report in .rtf format
	 */
	public void exportReport() throws JRException
	{
		progressMonitor = (JRExportProgressMonitor)parameters.get(JRExporterParameter.PROGRESS_MONITOR);

		/*   */
		setOffset();

		try
		{
			/*   */
			setExportContext();

			/*   */
			setInput();
			
			if (!parameters.containsKey(JRExporterParameter.FILTER))
			{
				filter = createFilter(RTF_EXPORTER_PROPERTIES_PREFIX);
			}

			if (!isModeBatch) {
				setPageRange();
			}

			fonts = new ArrayList();
			colors = new ArrayList();
			colors.add(null);

			fontMap = (Map) parameters.get(JRExporterParameter.FONT_MAP);
			setHyperlinkProducerFactory();
			StringBuffer sb = (StringBuffer)parameters.get(JRExporterParameter.OUTPUT_STRING_BUFFER);
			if (sb != null) {
				StringBuffer buffer = exportReportToBuffer();
				sb.append(buffer.toString());
			}
			else {
				Writer outWriter = (Writer)parameters.get(JRExporterParameter.OUTPUT_WRITER);
				if (outWriter != null) {
					try {
						rtfWriter = outWriter;

						// export report
						exportReportToStream();
					}
					catch (IOException ex) {
						throw new JRException("Error writing to writer : " + jasperPrint.getName(), ex);
					}
				}
				else {
					OutputStream os = (OutputStream)parameters.get(JRExporterParameter.OUTPUT_STREAM);
					if(os != null) {
						try {
							rtfWriter = new OutputStreamWriter(os);

							// export report
							exportReportToStream();
						}
						catch (Exception ex) {
							throw new JRException("Error writing to output stream : " + jasperPrint.getName(), ex);
						}
					}
					else {
						destFile = (File)parameters.get(JRExporterParameter.OUTPUT_FILE);
						if (destFile == null) {
							String fileName = (String)parameters.get(JRExporterParameter.OUTPUT_FILE_NAME);
							if (fileName != null) {
								destFile = new File(fileName);
							}
							else {
								throw new JRException("No output specified for the exporter");
							}
						}

						// export report
						exportReportToFile();
					}
				}
			}
		}
		finally
		{
			resetExportContext();
		}
	}

	/**
	 * Export report in .rtf format
	 * @return report in .rtf format in a StringBuffer object
	 */
	protected StringBuffer exportReportToBuffer() throws JRException{
		StringWriter buffer = new StringWriter();
		rtfWriter = buffer;
		try {
			exportReportToStream();
		}
		catch (IOException ex) {
			throw new JRException("Error while exporting report to the buffer", ex);
		}

		return buffer.getBuffer();
	}


	/**
	 * Export report in .rtf format to a stream
	 * @throws JRException
	 * @throws IOException
	 */
	protected void exportReportToStream() throws JRException, IOException 
	{
		colorWriter = new FileBufferedWriter();
		fontWriter = new FileBufferedWriter();
		writer = new FileBufferedWriter();

//		for (String propName : jasperPrint.getPropertyNames()) {
//			System.out.println("jasperPrint propName = " + propName + "   propVal = " +jasperPrint.getProperty(propName));
//		}
		
		for(reportIndex = 0; reportIndex < jasperPrintList.size(); reportIndex++ ){
			setJasperPrint((JasperPrint)jasperPrintList.get(reportIndex));

			List pages = jasperPrint.getPages();
		    if  (jasperPrint.getProperty(IGNORE_PAGINATION_PROPERTY) != null &&
		    		jasperPrint.getProperty(IGNORE_PAGINATION_PROPERTY).equals("true")
		         ){
		    	ignorePagination = true;
		    }
		    
			if (pages != null && pages.size() > 0){
				if (isModeBatch)
				{
					startPageIndex = 0;
					endPageIndex = pages.size() - 1;
				}
				JRPrintPage page = null;
                if(!ignorePagination) {
					writer.write("{\\info{\\nofpages");
					writer.write(String.valueOf(pages.size()));
					writer.write("}}\n");
                }

				writer.write("\\viewkind1\\paperw");
				writer.write(String.valueOf(twip(jasperPrint.getPageWidth())));
				writer.write("\\paperh");
				writer.write(String.valueOf(twip(jasperPrint.getPageHeight())));

				writer.write("\\marglsxn");
				writer.write(String.valueOf(twip(jasperPrint.getLeftMargin() == null ? 0 : jasperPrint.getLeftMargin())));
				writer.write("\\margrsxn");
				writer.write(String.valueOf(twip(jasperPrint.getRightMargin() == null ? 0 : jasperPrint.getRightMargin())));
				writer.write("\\margtsxn");
				writer.write(String.valueOf(twip(jasperPrint.getTopMargin() == null ? 0 : jasperPrint.getTopMargin())));
				writer.write("\\margbsxn");
				writer.write(String.valueOf(twip(jasperPrint.getBottomMargin() == null ? 0 : jasperPrint.getBottomMargin())));

				if (jasperPrint.getOrientationValue() == OrientationEnum.LANDSCAPE) {
					writer.write("\\lndscpsxn");
				}


				for (int pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++) {
					writer.write("\n");
					if(Thread.interrupted()){
						throw new JRException("Current thread intrerrupted");
					}

					page = (JRPrintPage)pages.get(pageIndex);
					writeAnchor(JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + (pageIndex + 1));

					boolean lastPageFlag = false;
					if(pageIndex == endPageIndex && reportIndex == (jasperPrintList.size() - 1)){
						lastPageFlag = true;
					}
					exportPage(page, lastPageFlag);
				}
			}
		}
		writer.write("}\n");

		writer.close();
		colorWriter.close();
		fontWriter.close();
		
		// create the header of the rtf file
		rtfWriter.write("{\\rtf1\\ansi\\deff0\n");
		// create font and color tables
		rtfWriter.write("{\\fonttbl ");
		fontWriter.writeData(rtfWriter);
		rtfWriter.write("}\n");

		rtfWriter.write("{\\colortbl ;");
		colorWriter.writeData(rtfWriter);
		rtfWriter.write("}\n");

		writer.writeData(rtfWriter);

		rtfWriter.flush();

		writer.dispose();
		colorWriter.dispose();
		fontWriter.dispose();
	}


	/**
	 * Export report to a file in the .rtf format
	 */
	protected void exportReportToFile() throws JRException {
		try {
			OutputStream fileOutputStream = new FileOutputStream(destFile);
			rtfWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
			exportReportToStream();
		}
		catch (IOException ex) {
			throw new JRException("Error writing to the file : " + destFile, ex);
		}
		finally {
			if(rtfWriter != null) {
				try {
					rtfWriter.close();
				}
				catch(IOException ex) {

				}
			}
		}
	}


	/**
	 * Return color index from header of the .rtf file. If a color is not
	 * found is automatically added to the header of the rtf file. The
	 * method is called first when the header of the .rtf file is constructed
	 * and when a component needs a color for foreground or background
	 * @param color Color for which the index is required.
	 * @return index of the color from .rtf file header
	 */
	private int getColorIndex(Color color) throws IOException
	{
		int colorNdx = colors.indexOf(color);
		if (colorNdx < 0)
		{
			colorNdx = colors.size();
			colors.add(color);
			colorWriter.write(
				"\\red" + color.getRed() 
				+ "\\green" + color.getGreen() 
				+ "\\blue" + color.getBlue() + ";"
				);
		}
		return colorNdx;
	}


	/**
	 * Return font index from the header of the .rtf file. The method is
	 * called first when the header of the .rtf document is constructed and when a
	 * text component needs font informations.
	 * @param font the font for which the index is required
	 * @return index of the font from .rtf file header
	 */
	private int getFontIndex(JRFont font, Locale locale) throws IOException
	{
		String fontName = font.getFontName();
		if(fontMap != null && fontMap.containsKey(fontName))
		{
			fontName = (String)fontMap.get(fontName);
		}
		else
		{
			FontInfo fontInfo = JRFontUtil.getFontInfo(fontName, locale);
			if (fontInfo != null)
			{
				//fontName found in font extensions
				FontFamily family = fontInfo.getFontFamily();
				String exportFont = family.getExportFont(getExporterKey());
				if (exportFont != null)
				{
					fontName = exportFont;
				}
			}
		}

		int fontIndex = fonts.indexOf(fontName);

		if(fontIndex < 0) {
			fontIndex = fonts.size();
			fonts.add(fontName);
			fontWriter.write("{\\f"  + fontIndex + "\\fnil " + fontName + ";}");
		}

		return fontIndex;
	}

	/**
	 * Convert a int value from points to EMU (multiply with 12700)
	 * @param points value that needs to be converted
	 * @return converted value in EMU
	 */
	private int emu(float points) {
		return (int)(points * 12700);
	}


	/**
	 * Convert a float value to twips (multiply with 20)
	 * @param points value that need to be converted
	 * @return converted value in twips
	 */
	private int twip(float points) {
		return (int)(points * 20);
	}


	/**
	 * Exports a report page
	 * @param page Page that will be exported
	 * @throws JRException
	 */
	protected void exportPage(JRPrintPage page, boolean lastPage) throws JRException, IOException
	{
		exportElements(page.getElements());

		if(!lastPage && !ignorePagination)
		{
			writer.write("\\page\n");
		}
	}
	
	private void startElementParagraph(JRPrintElement element) throws IOException 
	{
		// System.out.println("jasperPrint.getPageWidth() = " + jasperPrint.getPageWidth());
		// System.out.println("element.getX() = " + element.getX());
		// System.out.println("getOffsetX() = " + getOffsetX());
		// System.out.println("element.getWidth() = " + element.getWidth());
		// System.out.println("jasperPrint.getRightMargin() = " + jasperPrint.getRightMargin());
		// System.out.println("Calc = " + (jasperPrint.getPageWidth()-jasperPrint.getRightMargin()- element.getX() - element.getWidth()));
		
		// System.out.println("Left = " + String.valueOf(twip(element.getX() + getOffsetX()-jasperPrint.getTopMargin())));
		// System.out.println("Rigth = "+ String.valueOf(twip(element.getX() + getOffsetX() + element.getWidth()-jasperPrint.getRightMargin())));
		// System.out.println("Top = " + String.valueOf(element.getY() + getOffsetY()));
		// System.out.println("Bottom = " + String.valueOf(element.getY() + getOffsetY() + element.getHeight()));
		// System.out.println("Left margin = " + jasperPrint.getLeftMargin());
		// System.out.println("Right margin = " + jasperPrint.getRightMargin());
		// System.out.println("Top margin = " + jasperPrint.getTopMargin());
		// System.out.println("Bottom margin = " + jasperPrint.getBottomMargin());
		
		
		// System.out.println("Text = " + ((JRPrintText)element).getFullText());
		writer.write("{\\pard");
		writer.write("\\li");
		writer.write(String.valueOf(twip(element.getX() + getOffsetX()-jasperPrint.getTopMargin())));
		writer.write("\\ri");
		writer.write(String.valueOf(twip(jasperPrint.getPageWidth()-jasperPrint.getRightMargin()- element.getX() - element.getWidth())));
//		writer.write("\\sb");
//		writer.write(String.valueOf(twip(element.getY() + getOffsetY()-56)));
//		writer.write("\\sa");
//		writer.write(String.valueOf(twip(element.getY() + getOffsetY() + element.getHeight()-56)));

//		Color bgcolor = element.getBackcolor();
//
//		if (element.getModeValue() == ModeEnum.OPAQUE)
//		{
//			writer.write("{\\sp{\\sn fFilled}{\\sv 1}}");
//			writer.write("{\\sp{\\sn fillColor}{\\sv ");
//			writer.write(String.valueOf(getColorRGB(bgcolor)));
//			writer.write("}}");
//		}
//		else
//		{
//			writer.write("{\\sp{\\sn fFilled}{\\sv 0}}");
//		}
//		
//		writer.write("{\\shpinst");
	}
	
	/**
	 * 
	 * @param element
	 * @param listTemplateId - v obshtiq sluchai e ls1. 
	 *     The (1-based) index of this \listoverride in the \listoverride table. This value should never be zero inside a \listoverride, and must be unique for all \listoverrides within a document. The valid values are from 1 to 2000.
	 *     Definira se v  {\\*\\listoverridetable{\\listoverride\\listid392318758\\ls1}}
	 * @throws IOException
	 */
	private void startNumberedElement(JRPrintElement element, String listTemplateId) throws IOException 
    {
 
        writer.write("{\\pard\\fi-360\\li920\\ri0\\" + listTemplateId);
        writer.write("\\li");
        writer.write(String.valueOf(twip(element.getX() + getOffsetX()-jasperPrint.getTopMargin() + 46 ))); //otmestvaneto e 1 cm v dqsno...//Tuk e topMargin po podobie na startElementParagraph, vypreki che i na 2te mesta e greshno!
        writer.write("\\fi-360"); //otmestvaneto na pyrviq red (tozi s nomera) e 18 pixels nalqva ot li 
        
        writer.write("\\ri");
        writer.write(String.valueOf(twip(jasperPrint.getPageWidth()-jasperPrint.getRightMargin()- element.getX() - element.getWidth())));
        writer.write("\\" + listTemplateId);
    }



	/**
	 *
	 */
	private void startElement(JRPrintElement element) throws IOException {
		int shapeType = _getShapeType(element);
		boolean  yRelativeToParagraph = isYRelativeToParagraph(element);
		String shpby = yRelativeToParagraph ? "shpbypara" : "shpbypage";
		writer.write("{\\shp\\shpbxpage\\"+ shpby + "\\shpwr" + shapeType + "\\shpfhdr0\\shpfblwtxt0\\shpz");
		writer.write(String.valueOf(zorder++));
		writer.write("\\shpleft");
		writer.write(String.valueOf(twip(element.getX() + getOffsetX())));
		writer.write("\\shpright");
		writer.write(String.valueOf(twip(element.getX() + getOffsetX() + element.getWidth())));
		writer.write("\\shptop");
		int top = getOffsetY() + (yRelativeToParagraph ? 0 : element.getY());
		writer.write(String.valueOf(twip(top)));
		writer.write("\\shpbottom");
		writer.write(String.valueOf(twip(top + element.getHeight())));

		Color bgcolor = element.getBackcolor();

		if (element.getModeValue() == ModeEnum.OPAQUE)
		{
			writer.write("{\\sp{\\sn fFilled}{\\sv 1}}");
			writer.write("{\\sp{\\sn fillColor}{\\sv ");
			writer.write(String.valueOf(getColorRGB(bgcolor)));
			writer.write("}}");
		}
		else
		{
			writer.write("{\\sp{\\sn fFilled}{\\sv 0}}");
		}
		
		writer.write("{\\shpinst");
	}

	/**
	 * Describes the type of wrap for the shape.
	 * 1 Wrap around top and bottom of shape (no text allowed beside shape)
	 * 2 Wrap around shape
	 * 3 None (wrap as if shape isn't present)
	 * 4 Wrap tightly around shape
	 * 5 Wrap text through shape
	 * @param element
	 * @return
	 */
	private int _getShapeType(JRPrintElement element) {
		String shapeType = element.getPropertiesMap().getProperty("net.sf.jasperreports.export.shape.type");
		return shapeType == null ? 5 : DataConverter.parseInteger(shapeType, 5);
	}

	private boolean isYRelativeToParagraph(JRPrintElement element) {
		String relativeToParagraph = element.getPropertiesMap().getProperty("net.sf.jasperreports.export.y.relative.to.paragraph");
		return DataConverter.parseBoolean(relativeToParagraph);
	}

	/**
	 *
	 */
	private int getColorRGB(Color color) 
	{
		return color.getRed() + 256 * color.getGreen() + 65536 * color.getBlue();
	}

	/**
	 *
	 */
	private void finishElement() throws IOException 
	{
		writer.write("}}\n");
	}

	/**
	 *
	 */
	private void exportPen(JRPen pen) throws IOException 
	{
		writer.write("{\\sp{\\sn lineColor}{\\sv ");
		writer.write(String.valueOf(getColorRGB(pen.getLineColor())));
		writer.write("}}");

		float lineWidth = pen.getLineWidth().floatValue();
		
		if (lineWidth == 0f)
		{
			writer.write("{\\sp{\\sn fLine}{\\sv 0}}");
		}

		switch (pen.getLineStyleValue())
		{
			case DOUBLE :
			{
				writer.write("{\\sp{\\sn lineStyle}{\\sv 1}}");
				break;
			}
			case DOTTED :
			{
				writer.write("{\\sp{\\sn lineDashing}{\\sv 2}}");
				break;
			}
			case DASHED :
			{
				writer.write("{\\sp{\\sn lineDashing}{\\sv 1}}");
				break;
			}
		}

		writer.write("{\\sp{\\sn lineWidth}{\\sv ");
		writer.write(String.valueOf(emu(lineWidth)));
		writer.write("}}");
	}


	/**
	 *
	 */
	private void exportPen(Color color) throws IOException 
	{
		writer.write("{\\sp{\\sn lineColor}{\\sv ");
		writer.write(String.valueOf(getColorRGB(color)));
		writer.write("}}");
		writer.write("{\\sp{\\sn fLine}{\\sv 0}}");
		writer.write("{\\sp{\\sn lineWidth}{\\sv 0}}");
	}


	/**
	 * Draw a line object
	 * @param line JasperReports line object - JRPrintLine
	 * @throws IOException
	 */
	protected void exportLine(JRPrintLine line) throws IOException 
	{
		int x = line.getX() + getOffsetX();
		int y = line.getY() + getOffsetY();
		int height = line.getHeight();
		int width = line.getWidth();

		if (width <= 1 || height <= 1)
		{
			if (width > 1)
			{
				height = 0;
			}
			else
			{
				width = 0;
			}
		}

		writer.write("{\\shp\\shpbxpage\\shpbypage\\shpwr5\\shpfhdr0\\shpz");
		writer.write(String.valueOf(zorder++));
		writer.write("\\shpleft");
		writer.write(String.valueOf(twip(x)));
		writer.write("\\shpright");
		writer.write(String.valueOf(twip(x + width)));
		writer.write("\\shptop");
		writer.write(String.valueOf(twip(y)));
		writer.write("\\shpbottom");
		writer.write(String.valueOf(twip(y + height)));

		writer.write("{\\shpinst");
		
		writer.write("{\\sp{\\sn shapeType}{\\sv 20}}");
		
		exportPen(line.getLinePen());
		
		if (line.getDirectionValue() == LineDirectionEnum.TOP_DOWN)
		{
			writer.write("{\\sp{\\sn fFlipV}{\\sv 0}}");
		}
		else
		{
			writer.write("{\\sp{\\sn fFlipV}{\\sv 1}}");
		}

		writer.write("}}\n");
	}


	/**
	 *
	 */
	private void exportBorder(JRPen pen, float x, float y, float width, float height) throws IOException 
	{
		writer.write("{\\shp\\shpbxpage\\shpbypage\\shpwr5\\shpfhdr0\\shpz");
		writer.write(String.valueOf(zorder++));
		writer.write("\\shpleft");
		writer.write(String.valueOf(twip(x)));//FIXMEBORDER starting point of borders seem to have CAP_SQUARE-like appearence at least for Thin
		writer.write("\\shpright");
		writer.write(String.valueOf(twip(x + width)));
		writer.write("\\shptop");
		writer.write(String.valueOf(twip(y)));
		writer.write("\\shpbottom");
		writer.write(String.valueOf(twip(y + height)));

		writer.write("{\\shpinst");
		
		writer.write("{\\sp{\\sn shapeType}{\\sv 20}}");
		
		exportPen(pen);
		
		writer.write("}}\n");
	}


	/**
	 * Draw a rectangle
	 * @param rectangle JasperReports rectangle object (JRPrintRectangle)
	 */
	protected void exportRectangle(JRPrintRectangle rectangle) throws IOException 
	{
		startElement(rectangle);
		
		if (rectangle.getRadius() == 0)
		{
			writer.write("{\\sp{\\sn shapeType}{\\sv 1}}");
		}
		else
		{
			writer.write("{\\sp{\\sn shapeType}{\\sv 2}}");
		}

		exportPen(rectangle.getLinePen());
		
		finishElement();
	}


	/**
	 * Draw a ellipse object
	 * @param ellipse JasperReports ellipse object (JRPrintElipse)
	 */
	protected void exportEllipse(JRPrintEllipse ellipse) throws IOException 
	{
		startElement(ellipse);
		
		writer.write("{\\sp{\\sn shapeType}{\\sv 3}}");

		exportPen(ellipse.getLinePen());
		
		finishElement();
	}

	public void exportText(JRPrintText text) throws IOException, JRException {
		//System.out.println(text.getOrigin().getBandTypeValue() + 
		//		" exporting " + (text.getOrigin().getBandTypeValue() == BandTypeEnum.DETAIL ? " Paragraph " : " Shape ") + 
		//		 text.getFullText());
		//System.out.println("text.getKey() =  " + text.getKey());
		if(text.getOrigin().getBandTypeValue() == BandTypeEnum.PAGE_FOOTER){
			 exportTextShape(text);
		} else {
			 exportTextParagraph(text);
		}
	}

	/**
	 * Draw a text box
	 * @param text JasperReports text object (JRPrintText)
	 * @throws JRException
	 */
	public void exportTextShape(JRPrintText text) throws IOException, JRException {


		// use styled text
		JRStyledText styledText = getStyledText(text);
		if (styledText == null)
		{
			return;
		}

		int width = text.getWidth();
		int height = text.getHeight();

		int textHeight = (int)text.getTextHeight();

		if(textHeight <= 0) {
			if(height <= 0 ){
				throw new JRException("Invalid text height");
			}
			textHeight = height;
		}

		/*   */
		startElement(text);

		// padding for the text
		int topPadding = text.getLineBox().getTopPadding().intValue();
		int leftPadding = text.getLineBox().getLeftPadding().intValue();
		int bottomPadding = text.getLineBox().getBottomPadding().intValue();
		int rightPadding = text.getLineBox().getRightPadding().intValue();

		String rotation = null;

		switch (text.getRotationValue())
		{
			case LEFT :
			{
				switch (text.getVerticalAlignmentValue())
				{
					case TOP:
					{
						break;
					}
					case MIDDLE:
					{
						leftPadding = Math.max(leftPadding, (width - rightPadding - textHeight) / 2);
						break;
					}
					case BOTTOM:
					{
						leftPadding = Math.max(leftPadding, width - rightPadding - textHeight);
						break;
					}
				}
				rotation = "{\\sp{\\sn txflTextFlow}{\\sv 2}}";
				break;
			}
			case RIGHT :
			{
				switch (text.getVerticalAlignmentValue())
				{
					case TOP:
					{
						break;
					}
					case MIDDLE:
					{
						rightPadding = Math.max(rightPadding, (width - leftPadding - textHeight) / 2);
						break;
					}
					case BOTTOM:
					{
						rightPadding = Math.max(rightPadding, width - leftPadding - textHeight);
						break;
					}
				}
				rotation = "{\\sp{\\sn txflTextFlow}{\\sv 3}}";
				break;
			}
			case UPSIDE_DOWN :
			{
				switch (text.getVerticalAlignmentValue())
				{
					case TOP:
					{
						topPadding = Math.max(topPadding, height - bottomPadding - textHeight);
						break;
					}
					case MIDDLE:
					{
						topPadding = Math.max(topPadding, (height - bottomPadding - textHeight) / 2);
						break;
					}
					case BOTTOM:
					{
						break;
					}
				}
				rotation = "";
				break;
			}
			case NONE :
			default :
			{
				switch (text.getVerticalAlignmentValue())
				{
					case TOP:
					{
						break;
					}
					case MIDDLE:
					{
						topPadding = Math.max(topPadding, (height - bottomPadding - textHeight) / 2);
						break;
					}
					case BOTTOM:
					{
						topPadding = Math.max(topPadding, height - bottomPadding - textHeight);
						break;
					}
				}
				rotation = "";
			}
		}

		writer.write(rotation);
		writer.write("{\\sp{\\sn dyTextTop}{\\sv ");
		writer.write(String.valueOf(emu(topPadding)));
		writer.write("}}");
		writer.write("{\\sp{\\sn dxTextLeft}{\\sv ");
		writer.write(String.valueOf(emu(leftPadding)));
		writer.write("}}");
		writer.write("{\\sp{\\sn dyTextBottom}{\\sv ");
		writer.write(String.valueOf(emu(bottomPadding)));
		writer.write("}}");
		writer.write("{\\sp{\\sn dxTextRight}{\\sv ");
		writer.write(String.valueOf(emu(rightPadding)));
		writer.write("}}");
		writer.write("{\\sp{\\sn fLine}{\\sv 0}}");
		writer.write("{\\shptxt{\\pard");
		
		JRFont font = text;
		if (text.getRunDirectionValue() == RunDirectionEnum.RTL)
		{
			writer.write("\\rtlch");
		}
//		writer.write("\\f");
//		writer.write(String.valueOf(getFontIndex(font)));
//		writer.write("\\cf");
//		writer.write(String.valueOf(getColorIndex(text.getForecolor())));
		writer.write("\\cb");
		writer.write(String.valueOf(getColorIndex(text.getBackcolor())));
		writer.write(" ");

//		if (font.isBold())
//			writer.write("\\b");
//		if (font.isItalic())
//			writer.write("\\i");
//		if (font.isStrikeThrough())
//			writer.write("\\strike");
//		if (font.isUnderline())
//			writer.write("\\ul");
//		writer.write("\\fs");
//		writer.write(String.valueOf(font.getFontSize() * 2));

		switch (text.getHorizontalAlignmentValue())
		{
			case LEFT:
				writer.write("\\ql");
				break;
			case CENTER:
				writer.write("\\qc");
				break;
			case RIGHT:
				writer.write("\\qr");
				break;
			case JUSTIFIED:
				writer.write("\\qj");
				break;
			default:
				writer.write("\\ql");
				break;
		}

		writer.write("\\sl");
		writer.write(String.valueOf(twip(text.getLineSpacingFactor() * font.getFontSize())));
		writer.write(" ");

		if (text.getAnchorName() != null)
		{
			writeAnchor(text.getAnchorName());
		}

		exportHyperlink(text);

		// add parameters in case of styled text element
		String plainText = styledText.getText();
		int runLimit = 0;

		AttributedCharacterIterator iterator = styledText.getAttributedString().getIterator();
		while (
			runLimit < styledText.length()
			&& (runLimit = iterator.getRunLimit()) <= styledText.length()
			)
		{

			Map styledTextAttributes = iterator.getAttributes();
			JRFont styleFont = new JRBaseFont(styledTextAttributes);
			Color styleForeground = (Color) styledTextAttributes.get(TextAttribute.FOREGROUND);
			Color styleBackground = (Color) styledTextAttributes.get(TextAttribute.BACKGROUND);

			writer.write("\\f");
			writer.write(String.valueOf(getFontIndex(styleFont, getTextLocale(text))));
			writer.write("\\fs");
			writer.write(String.valueOf(2 * styleFont.getFontSize()));

			if (styleFont.isBold())
			{
				writer.write("\\b");
			}
			if (styleFont.isItalic())
			{
				writer.write("\\i");
			}
			if (styleFont.isUnderline())
			{
				writer.write("\\ul");
			}
			if (styleFont.isStrikeThrough())
			{
				writer.write("\\strike");
			}

			if (TextAttribute.SUPERSCRIPT_SUPER.equals(styledTextAttributes.get(TextAttribute.SUPERSCRIPT)))
			{
				writer.write("\\super");
			}
			else if (TextAttribute.SUPERSCRIPT_SUB.equals(styledTextAttributes.get(TextAttribute.SUPERSCRIPT)))
			{
				writer.write("\\sub");
			}

			if(!(null == styleBackground || styleBackground.equals(text.getBackcolor()))){
				writer.write("\\highlight");
				writer.write(String.valueOf(getColorIndex(styleBackground)));
			}
			writer.write("\\cf");
			writer.write(String.valueOf(getColorIndex(styleForeground)));
			writer.write(" ");

			writer.write(
				handleUnicodeText(
					plainText.substring(iterator.getIndex(), runLimit)					
					)
				);

			// reset all styles in the paragraph
			writer.write("\\plain");

			iterator.setIndex(runLimit);
		}
//		if (startedHyperlink)
//		{
//			endHyperlink();
//		}

		writer.write("\\par}}");
		
		/*   */
		finishElement();

		exportBox(text.getLineBox(), text.getX() + getOffsetX(), text.getY() + getOffsetY(), width, height);
	}
	
	/**
	 * Draw a text box
	 * @param text JasperReports text object (JRPrintText)
	 * @throws JRException
	 * 
	 * TODO:Trqbva tuk da se butne za da rabotqt nomeraciite!!!
	 */
	public void exportTextParagraph(JRPrintText text) throws IOException, JRException {


		// use styled text
		JRStyledText styledText = getStyledText(text);
		if (styledText == null)
		{
			return;
		}

		
		
		int width = text.getWidth();
		int height = text.getHeight();

		int textHeight = (int)text.getTextHeight();

		if(textHeight <= 0) {
			if(height <= 0 ){
				throw new JRException("Invalid text height");
			}
			textHeight = height;
		}

		String plainText = styledText.getText();
		//boolean isListItem = false;

        Matcher m = CUSTOM_LIST_ITEM_PATTERN.matcher(plainText);
        if (plainText.equals("<cust-ol>")) {
            writer.write("{\\*\\listtable{\\list\\listhybrid{\\listlevel\\levelnfc0\\levelnfcn0\\leveljc0\\leveljcn0\\levelstartat1{\\leveltext\\'02\\'00.;}{\\levelnumbers\\'01;}}\\listid392318758}}\n");
            writer.write("{\\*\\listoverridetable{\\listoverride\\listid392318758\\ls1}}\n");
            return;
        } else if (plainText.equals("</cust-ol>")) {
            return;
        } else if (m.find()) { //custom list item...
            startNumberedElement(text, "ls1");
            //plainText = m.group(1);
            //isListItem = true;
        } else {
            startElementParagraph(text);    
        }
        
		/*   */
		
		// padding for the text
		int topPadding = text.getLineBox().getTopPadding().intValue();
		int leftPadding = text.getLineBox().getLeftPadding().intValue();
		int bottomPadding = text.getLineBox().getBottomPadding().intValue();
		int rightPadding = text.getLineBox().getRightPadding().intValue();

		String rotation = null;

		switch (text.getRotationValue())
		{
			case LEFT :
			{
				switch (text.getVerticalAlignmentValue())
				{
					case TOP:
					{
						break;
					}
					case MIDDLE:
					{
						leftPadding = Math.max(leftPadding, (width - rightPadding - textHeight) / 2);
						break;
					}
					case BOTTOM:
					{
						leftPadding = Math.max(leftPadding, width - rightPadding - textHeight);
						break;
					}
				}
				rotation = "{\\sp{\\sn txflTextFlow}{\\sv 2}}";
				break;
			}
			case RIGHT :
			{
				switch (text.getVerticalAlignmentValue())
				{
					case TOP:
					{
						break;
					}
					case MIDDLE:
					{
						rightPadding = Math.max(rightPadding, (width - leftPadding - textHeight) / 2);
						break;
					}
					case BOTTOM:
					{
						rightPadding = Math.max(rightPadding, width - leftPadding - textHeight);
						break;
					}
				}
				rotation = "{\\sp{\\sn txflTextFlow}{\\sv 3}}";
				break;
			}
			case UPSIDE_DOWN :
			{
				switch (text.getVerticalAlignmentValue())
				{
					case TOP:
					{
						topPadding = Math.max(topPadding, height - bottomPadding - textHeight);
						break;
					}
					case MIDDLE:
					{
						topPadding = Math.max(topPadding, (height - bottomPadding - textHeight) / 2);
						break;
					}
					case BOTTOM:
					{
						break;
					}
				}
				rotation = "";
				break;
			}
			case NONE :
			default :
			{
				switch (text.getVerticalAlignmentValue())
				{
					case TOP:
					{
						break;
					}
					case MIDDLE:
					{
						topPadding = Math.max(topPadding, (height - bottomPadding - textHeight) / 2);
						break;
					}
					case BOTTOM:
					{
						topPadding = Math.max(topPadding, height - bottomPadding - textHeight);
						break;
					}
				}
				rotation = "";
			}
		}

        writeTextContent(text);


		writer.write("\\par}\n");
		
		/*   */
		//finishElement();

		exportBox(text.getLineBox(), text.getX() + getOffsetX(), text.getY() + getOffsetY(), width, height);
	}

    private void writeTextContent(JRPrintText text) throws IOException {


        JRFont font = text;
        JRStyledText styledText = getStyledText((JRPrintText) text);
        String plainText = styledText.getText();


        if (text.getRunDirectionValue() == RunDirectionEnum.RTL) {
            writer.write("\\rtlch");
        }
        writer.write("\\cb");
        writer.write(String.valueOf(getColorIndex(text.getBackcolor())));
        writer.write(" ");

        switch (text.getHorizontalAlignmentValue()) {
            case LEFT:
                writer.write("\\ql");
                break;
            case CENTER:
                writer.write("\\qc");
                break;
            case RIGHT:
                writer.write("\\qr");
                break;
            case JUSTIFIED:
                writer.write("\\qj");
                break;
            default:
                writer.write("\\ql");
                break;
        }

        writer.write("\\sl");
        writer.write(String.valueOf(twip(text.getLineSpacingFactor() * font.getFontSize())));
        writer.write(" ");

        if (text.getAnchorName() != null) {
            writeAnchor(text.getAnchorName());
        }

        exportHyperlink(text);

        // add parameters in case of styled text element

        int runLimit = 0;

        AttributedCharacterIterator iterator = styledText.getAttributedString().getIterator();
        while (
                runLimit < styledText.length()
                        && (runLimit = iterator.getRunLimit()) <= styledText.length()
                ) {

            Map styledTextAttributes = iterator.getAttributes();
            JRFont styleFont = new JRBaseFont(styledTextAttributes);
            Color styleForeground = (Color) styledTextAttributes.get(TextAttribute.FOREGROUND);
            Color styleBackground = (Color) styledTextAttributes.get(TextAttribute.BACKGROUND);

            writer.write("\\f");
            writer.write(String.valueOf(getFontIndex(styleFont, getTextLocale(text))));
            writer.write("\\fs");
            writer.write(String.valueOf(2 * styleFont.getFontSize()));

            if (styleFont.isBold()) {
                writer.write("\\b");
            }
            if (styleFont.isItalic()) {
                writer.write("\\i");
            }
            if (styleFont.isUnderline()) {
                writer.write("\\ul");
            }
            if (styleFont.isStrikeThrough()) {
                writer.write("\\strike");
            }

            if (TextAttribute.SUPERSCRIPT_SUPER.equals(styledTextAttributes.get(TextAttribute.SUPERSCRIPT))) {
                writer.write("\\super");
            } else if (TextAttribute.SUPERSCRIPT_SUB.equals(styledTextAttributes.get(TextAttribute.SUPERSCRIPT))) {
                writer.write("\\sub");
            }

            if (!(null == styleBackground || styleBackground.equals(text.getBackcolor()))) {
                writer.write("\\highlight");
                writer.write(String.valueOf(getColorIndex(styleBackground)));
            }
            writer.write("\\cf");
            writer.write(String.valueOf(getColorIndex(styleForeground)));
            writer.write(" ");

            String str = handleUnicodeText(
                    plainText.substring(iterator.getIndex(), runLimit)
            );


            //ako stava vypros za <cust-li>xxxx</cust-li>, togava tagovete <cust-li></cust-li> trqbva da se izchistqt
            Matcher m = CUSTOM_LIST_ITEM_PATTERN.matcher(str);
            if (m.find()) {
                str = m.group(1);
            }
            writer.write(str);

            // reset all styles in the paragraph
            writer.write("\\plain");

            iterator.setIndex(runLimit);
        }
    }

	/**
	 * Replace Unicode characters with RTF Unicode control words
	 * @param sourceText source text
	 * @return text with Unicode characters replaced
	 */
	private String handleUnicodeText(String sourceText)
	{
		StringBuffer unicodeText = new StringBuffer();
		
		for(int i = 0; i < sourceText.length(); i++ )
		{
			long ch = sourceText.charAt(i);
			if(ch > 127)
			{
				unicodeText.append("\\u" + ch + '?');
			}
			else if(ch == '\n')
			{
				unicodeText.append("\\line ");
			}
			else if(ch == '\\' || ch =='{' || ch =='}')
			{
				unicodeText.append('\\').append((char)ch);
			}
			else
			{
				unicodeText.append((char)ch);
			}
		}

		return unicodeText.toString();
	}


	/**
	 * Export a image object
	 * @param printImage JasperReports image object (JRPrintImage)
	 * @throws JRException
	 * @throws IOException
	 */
	protected void exportImage(JRPrintImage printImage) throws JRException, IOException {

		JRRenderable renderer = getRenderer(printImage);


		if (renderer != null) {

			ImageDimensions dimensions = new ImageDimensions(printImage, renderer);

			startElement(printImage);
			exportPen(printImage.getForecolor());//FIXMEBORDER should we have lineColor here, if at all needed?
			finishElement();
			/**
			 * {\\shp{\\*\\shpinst\\shpleft0\\shptop0\\shpright%d\\shpbottom%d\\shpwr3\\shpwrk0\\shpfblwtxt1{\\sp{\\sn fLine}{\\sv 0}}{\\sp{\\sn fBehindDocument}{\\sv 1}}{\\sp{\\sn pib}{\\sv
			 */
			int shapeType = _getShapeType(printImage);
			boolean  yRelativeToParagraph = isYRelativeToParagraph(printImage);
			String shpby = yRelativeToParagraph ? "shpbypara" : "shpbypage";
			writer.write("{\\shp{\\*\\shpinst\\shpbxpage\\" + shpby + "\\shpwr" + shapeType + "\\shpfhdr0\\shpfblwtxt0\\shpz");
			writer.write(String.valueOf(zorder++));
			writer.write("\\shpleft");
			writer.write(String.valueOf(twip(printImage.getX() + dimensions.leftPadding + dimensions.xoffset + getOffsetX())));
			writer.write("\\shpright");
			writer.write(String.valueOf(twip(printImage.getX() + dimensions.leftPadding + dimensions.xoffset + getOffsetX() + dimensions.imageWidth)));
			writer.write("\\shptop");
			int top = dimensions.topPadding + dimensions.yoffset + getOffsetY() + (yRelativeToParagraph ? 0 : printImage.getY());
			writer.write(String.valueOf(twip(top)));
			writer.write("\\shpbottom");
			writer.write(String.valueOf(twip(top + dimensions.imageHeight)));
			writer.write("{\\sp{\\sn shapeType}{\\sv 75}}");
			writer.write("{\\sp{\\sn fFilled}{\\sv 0}}");
			writer.write("{\\sp{\\sn fLockAspectRatio}{\\sv 0}}");

			writer.write("{\\sp{\\sn cropFromTop}{\\sv ");
			writer.write(String.valueOf(dimensions.cropTop));
			writer.write("}}");
			writer.write("{\\sp{\\sn cropFromLeft}{\\sv ");
			writer.write(String.valueOf(dimensions.cropLeft));
			writer.write("}}");
			writer.write("{\\sp{\\sn cropFromBottom}{\\sv ");
			writer.write(String.valueOf(dimensions.cropBottom));
			writer.write("}}");
			writer.write("{\\sp{\\sn cropFromRight}{\\sv ");
			writer.write(String.valueOf(dimensions.cropRight));
			writer.write("}}");
//			writer.write("{\\sp{\\sn fBehindDocument}{\\sv 1}}");
			if(printImage.getAnchorName() != null)
			{
				writeAnchor(printImage.getAnchorName());
			}
			
			exportHyperlink(printImage);
			
			writer.write("{\\sp{\\sn pib}{\\sv ");

			printImageContent(printImage, renderer);

			writer.write("\n}}");
			writer.write("}}\n");
		}

		int x = printImage.getX() + getOffsetX();
		int y = printImage.getY() + getOffsetY();
		int width = printImage.getWidth();
		int height = printImage.getHeight();

		if (
			printImage.getLineBox().getTopPen().getLineWidth().floatValue() <= 0f &&
			printImage.getLineBox().getLeftPen().getLineWidth().floatValue() <= 0f &&
			printImage.getLineBox().getBottomPen().getLineWidth().floatValue() <= 0f &&
			printImage.getLineBox().getRightPen().getLineWidth().floatValue() <= 0f
			)
		{
			if (printImage.getLinePen().getLineWidth().floatValue() > 0f)
			{
				exportPen(printImage.getLinePen(), x, y, width, height);
			}
		}
		else
		{
			exportBox(printImage.getLineBox(), x, y, width, height);
		}
	}

	private static class ImageDimensions {
		private int leftPadding;
		private int topPadding;
		private int rightPadding;
		private int bottomPadding;
		private int imageWidth = 0;
		private int imageHeight = 0;
		private int xoffset = 0;
		private int yoffset = 0;
		private int cropTop = 0;
		private int cropLeft = 0;
		private int cropBottom = 0;
		private int cropRight = 0;

		public ImageDimensions(JRPrintImage printImage, JRRenderable renderer) {
			leftPadding = printImage.getLineBox().getLeftPadding().intValue();
			topPadding = printImage.getLineBox().getTopPadding().intValue();
			rightPadding = printImage.getLineBox().getRightPadding().intValue();
			bottomPadding = printImage.getLineBox().getBottomPadding().intValue();

			int availableImageWidth = printImage.getWidth() - leftPadding - rightPadding;
			availableImageWidth = availableImageWidth < 0 ? 0 : availableImageWidth;

			int availableImageHeight = printImage.getHeight() - topPadding - bottomPadding;
			availableImageHeight = availableImageHeight < 0 ? 0 : availableImageHeight;

			int normalWidth = availableImageWidth;
			int normalHeight = availableImageHeight;

			switch (printImage.getScaleImageValue())
			{
				case CLIP:
				{
					switch (printImage.getHorizontalAlignmentValue())
					{
						case RIGHT :
						{
							cropLeft = 65536 * (- availableImageWidth + normalWidth) / availableImageWidth;
							cropRight = 0;
							break;
						}
						case CENTER :
						{
							cropLeft = 65536 * (- availableImageWidth + normalWidth) / availableImageWidth / 2;
							cropRight = cropLeft;
							break;
						}
						case LEFT :
						default :
						{
							cropLeft = 0;
							cropRight = 65536 * (- availableImageWidth + normalWidth) / availableImageWidth;
							break;
						}
					}
					switch (printImage.getVerticalAlignmentValue())
					{
						case TOP :
						{
							cropTop = 0;
							cropBottom = 65536 * (- availableImageHeight + normalHeight) / normalHeight;
							break;
						}
						case MIDDLE :
						{
							cropTop = 65536 * (- availableImageHeight + normalHeight) / normalHeight / 2;
							cropBottom = cropTop;
							break;
						}
						case BOTTOM :
						default :
						{
							cropTop = 65536 * (- availableImageHeight + normalHeight) / normalHeight;
							cropBottom = 0;
							break;
						}
					}
					imageWidth = availableImageWidth;
					imageHeight = availableImageHeight;
					break;
				}
				case FILL_FRAME:
				{
					normalWidth = availableImageWidth;
					normalHeight = availableImageHeight;
					imageWidth = availableImageWidth;
					imageHeight = availableImageHeight;
					break;
				}
				case RETAIN_SHAPE:
				default:
				{
					if (printImage.getHeight() > 0)
					{
						double ratio = (double) normalWidth / (double) normalHeight;

						if (ratio > (double) availableImageWidth / (double) availableImageHeight)
						{
							normalWidth = availableImageWidth;
							normalHeight = (int) (availableImageWidth / ratio);
						}
						else
						{
							normalWidth = (int) (availableImageHeight * ratio);
							normalHeight = availableImageHeight;
						}

						xoffset = (int) (getXAlignFactor(printImage) * (availableImageWidth - normalWidth));
						yoffset = (int) (getYAlignFactor(printImage) * (availableImageHeight - normalHeight));
						imageWidth = normalWidth;
						imageHeight = normalHeight;
					}

					break;
				}
			}
		}

	}

	private JRRenderable getRenderer(JRPrintImage printImage) throws JRException {
		JRRenderable renderer = printImage.getRenderer();
		if (renderer == null || renderer.getType() != JRRenderable.TYPE_IMAGE) {
			return null;
		}

		// Image renderers are all asked for their image data at some point.
		// Better to test and replace the renderer now, in case of lazy load error.
		renderer = JRImageRenderer.getOnErrorRendererForImageData(renderer, printImage.getOnErrorTypeValue());

		if (renderer.getType() == JRRenderable.TYPE_SVG) {
			renderer = new JRWrappingSvgRenderer(renderer, new Dimension(printImage.getWidth(), printImage.getHeight()), ModeEnum.OPAQUE == printImage.getModeValue() ? printImage.getBackcolor() : null);
		}

		return renderer;
	}

	private void printImageContent(JRPrintImage printImage, JRRenderable renderer) throws IOException, JRException {

		writer.write("{\\pict");
		ImageDimensions imageDimensions = new ImageDimensions(printImage, renderer);
		double scaleX = (printImage.getWidth() / renderer.getDimension().getWidth());
		double scaleY = (printImage.getHeight() / renderer.getDimension().getHeight());
		if (scaleX < 1 || scaleY < 1) {
			int scale = (int) (Math.min(scaleX, scaleY) * 100);
			writer.write("\\picscalex" + scale + "\\picscaley" + scale);
		}
		if (imageDimensions.cropBottom > 0) {
			writer.write("\\piccropb" + imageDimensions.cropBottom);
		}
		if (imageDimensions.cropLeft > 0) {
			writer.write("\\piccropl" + imageDimensions.cropLeft);
		}
		if (imageDimensions.cropRight > 0) {
			writer.write("\\piccropr" + imageDimensions.cropRight);
		}
		if (imageDimensions.cropTop > 0) {
			writer.write("\\piccropt" + imageDimensions.cropTop);
		}
		if (renderer.getImageType() == JRRenderable.IMAGE_TYPE_JPEG) {
			writer.write("\\jpegblip");
		}
		else {
			writer.write("\\pngblip");
		}

		writer.write("\n");

		ByteArrayInputStream bais = new ByteArrayInputStream(renderer.getImageData());

		int count = 0;
		int current = 0;
		while ((current = bais.read()) != -1)
		{
			String helperStr = Integer.toHexString(current);
			if (helperStr.length() < 2)
			{
				helperStr = "0" + helperStr;
			}
			writer.write(helperStr);
			count++;
			if (count == 64)
			{
				writer.write("\n");
				count = 0;
			}
		}

		writer.write("\n}");

	}

	/**
	 *
	 * @param frame
	 * @throws JRException
	 */
	protected void exportFrame(JRPrintFrame frame) throws JRException, IOException {
		int x = frame.getX() + getOffsetX();
		int y = frame.getY() + getOffsetY();
		int width = frame.getWidth();
		int height = frame.getHeight();

		startElement(frame);
		
		exportPen(frame.getForecolor());
		
		finishElement();

		setFrameElementsOffset(frame, false);
		exportElements(frame.getElements());
		restoreElementOffsets();

		exportBox(frame.getLineBox(), x, y, width, height);
	}


	protected void exportElements(Collection elements) throws JRException, IOException {

		List<JRPrintElement> headerElements = new ArrayList<>();
		List<JRPrintElement> footerElements = new ArrayList<>();
		Map<Integer, JRPrintElement>  paragraphElements = new TreeMap<>();
	    
		if (elements != null && elements.size() > 0) {

			for (Iterator it = elements.iterator(); it.hasNext();) {
				JRPrintElement element = (JRPrintElement)it.next();
				// System.out.println("element bandType="+element.getOrigin().getBandTypeValue());
				if (filter == null || filter.isToExport(element)) {
					//First export all elements   except header and footer
					if (element.getOrigin().getBandTypeValue() == BandTypeEnum.PAGE_HEADER) {
						headerElements.add(element);
					} else if (element.getOrigin().getBandTypeValue() == BandTypeEnum.PAGE_FOOTER) {
						footerElements.add(element);
					} else {
						//exportElement(element);
						paragraphElements.put(element.getY(), element);
					}
				}
			}
		}
		
		//Workaround - header only for first page
		//Export header
		//System.out.println("headerElements.size() = " + headerElements.size());
		//System.out.println("footerElements() = " + footerElements.size());
		//System.out.println("exp props = " + jasperPrint.getPropertiesMap(); 
		if(headerElements.size() > 0) {
			//writer.write("{\\header ");
			for (JRPrintElement element : headerElements) {
				exportElement(element);
			}
			//writer.write("}");
		}
		//Footers  for all pages
		//export footer
		if(footerElements.size() > 0) {
			writer.write("{\\footer ");
			for (JRPrintElement element : footerElements) {
				exportElement(element);
			}
			writer.write("}");
		}
		
		//export paragraph elements
		for(Integer key: paragraphElements.keySet()) {
			exportElement(paragraphElements.get(key));
		}

		
	}
	
	
	protected void exportElement(JRPrintElement element) throws JRException, IOException {
		if(element.getOrigin().getBandTypeValue() == BandTypeEnum.PAGE_FOOTER) {
			if (element instanceof JRPrintLine) {
				exportLine((JRPrintLine)element);
			}
			else if (element instanceof JRPrintRectangle) {
				exportRectangle((JRPrintRectangle)element);
			}
			else if (element instanceof JRPrintEllipse) {
				exportEllipse((JRPrintEllipse)element);
			}
			else if (element instanceof JRPrintImage) {
				exportImage((JRPrintImage)element);
			}
			else if (element instanceof JRPrintText) {
				exportText((JRPrintText)element);
			}
			else if (element instanceof JRPrintFrame) {
				exportFrame((JRPrintFrame)element);
//				exportTableRow((JRPrintFrame) element);
			}
			else if (element instanceof JRGenericPrintElement) {
				exportGenericElement((JRGenericPrintElement)element);
			}
		} else {//the header and the body of the template are processed here!
			if (element instanceof JRPrintText) {
				exportText((JRPrintText)element);
			} else if (element instanceof JRPrintFrame) {
                exportTableRow((JRPrintFrame) element);
            } else if (element instanceof JRPrintImage) {
				exportImage((JRPrintImage)element);
			}
		}
	}  


    private void exportTableRow(JRPrintFrame element) throws IOException, JRException {
        writer.write("{\\trowd");
        float summaryColumnWidth = 0;
        for (JRPrintElement el : (List<JRPrintElement>)element.getElements()) {
            summaryColumnWidth += el.getWidth();

            //setting cell border
            Float rightPenLineWidth = getLineWidth(element, (JRBoxContainer) el, JRLineBox::getRightPen);
            if (rightPenLineWidth!= null) {//right border
                writer.write("\\clbrdrr\\brdrs\\brdrw" + twip(rightPenLineWidth));
            }
			Float leftPenLineWidth = getLineWidth(element, (JRBoxContainer) el, JRLineBox::getLeftPen);
            if (leftPenLineWidth != null) {//left border
                writer.write("\\clbrdrl\\brdrs\\brdrw" + twip(leftPenLineWidth));
            }
			Float topPenLineWidth = getLineWidth(element, (JRBoxContainer) el, JRLineBox::getTopPen);
            if (topPenLineWidth != null) {//top border
                writer.write("\\clbrdrt\\brdrs\\brdrw" + twip(topPenLineWidth));
            }
			Float bottomPenLineWidth = getLineWidth(element, (JRBoxContainer) el, JRLineBox::getBottomPen);
            if (bottomPenLineWidth != null) {//bottom border
                writer.write("\\clbrdrb\\brdrs\\brdrw" + twip(bottomPenLineWidth));
            }

            writer.write("\\cellx" + twip(summaryColumnWidth));
        }
        for (JRPrintElement el : (List<JRPrintElement>)element.getElements()) {
			writer.write("\\pard\\intbl");

			JRLineBox lineBox = ((JRBoxContainer) el).getLineBox();
			if (lineBox.getLeftPadding() != null) {//left indent
				writer.write("\\li" + twip(lineBox.getLeftPadding()));
			}
			if (lineBox.getRightPadding() != null) {//right indent
				writer.write("\\ri" + twip(lineBox.getRightPadding()));
			}
			writer.write(" ");

            if (el instanceof JRPrintText) {
                writeTextContent((JRPrintText) el);
            } else if (el instanceof JRPrintImage) {
				printImageContent((JRPrintImage) el, getRenderer((JRPrintImage) el));
			} else {
            	throw new RuntimeException("Unknown element type: " + el.getClass());
			}
			writer.write("\\cell");

        }
        writer.write("\\row}");

    }

    private static Float getLineWidth(JRPrintFrame frame, JRBoxContainer childElement, Function<JRLineBox, JRBoxPen> topPenFunction) {
		JRBoxPen frameLineBox = topPenFunction.apply(frame.getLineBox());
		JRBoxPen childElementLineBox = topPenFunction.apply(childElement.getLineBox());
		if (frameLineBox.getLineWidth() != null && !Objects.equals(frameLineBox.getLineWidth(), Float.valueOf(0))) {
			return frameLineBox.getLineWidth();
		} else if (childElementLineBox.getLineWidth() != null && !Objects.equals(childElementLineBox.getLineWidth(), Float.valueOf(0))) {
			return childElementLineBox.getLineWidth();
		} else {
			return null;
		}

	}


	/**
	 *
	 */
	private void exportBox(JRLineBox box, int x, int y, int width, int height) throws IOException
	{
		exportTopPen(box.getTopPen(), box.getLeftPen(), box.getRightPen(), x, y, width, height);
		exportLeftPen(box.getTopPen(), box.getLeftPen(), box.getBottomPen(), x, y, width, height);
		exportBottomPen(box.getLeftPen(), box.getBottomPen(), box.getRightPen(), x, y, width, height);
		exportRightPen(box.getTopPen(), box.getBottomPen(), box.getRightPen(), x, y, width, height);
	}

	/**
	 *
	 */
	private void exportPen(JRPen pen, int x, int y, int width, int height) throws IOException
	{
		exportTopPen(pen, pen, pen, x, y, width, height);
		exportLeftPen(pen, pen, pen, x, y, width, height);
		exportBottomPen(pen, pen, pen, x, y, width, height);
		exportRightPen(pen, pen, pen, x, y, width, height);
	}

	/**
	 *
	 */
	private void exportTopPen(
		JRPen topPen, 
		JRPen leftPen, 
		JRPen rightPen, 
		int x, 
		int y, 
		int width, 
		int height
		) throws IOException
	{
		if (topPen.getLineWidth().floatValue() > 0f) 
		{
			exportBorder(
				topPen, 
				x - leftPen.getLineWidth().floatValue() / 2, 
				y, 
				width + (leftPen.getLineWidth().floatValue() + rightPen.getLineWidth().floatValue()) / 2, 
				0
				);
			//exportBorder(topPen, x, y + getAdjustment(topPen), width, 0);
		}
	}

	/**
	 *
	 */
	private void exportLeftPen(
		JRPen topPen, 
		JRPen leftPen, 
		JRPen bottomPen, 
		int x, 
		int y, 
		int width, 
		int height
		) throws IOException
	{
		if (leftPen.getLineWidth().floatValue() > 0f) 
		{
			exportBorder(
				leftPen, 
				x, 
				y - topPen.getLineWidth().floatValue() / 2, 
				0, 
				height + (topPen.getLineWidth().floatValue() + bottomPen.getLineWidth().floatValue()) / 2
				);
			//exportBorder(leftPen, x + getAdjustment(leftPen), y, 0, height);
		}
	}

	/**
	 *
	 */
	private void exportBottomPen(
		JRPen leftPen, 
		JRPen bottomPen, 
		JRPen rightPen, 
		int x, 
		int y, 
		int width, 
		int height
		) throws IOException
	{
		if (bottomPen.getLineWidth().floatValue() > 0f) 
		{
			exportBorder(
				bottomPen, 
				x - leftPen.getLineWidth().floatValue() / 2, 
				y + height, 
				width + (leftPen.getLineWidth().floatValue() + rightPen.getLineWidth().floatValue()) / 2, 
				0
				);
			//exportBorder(bottomPen, x, y + height - getAdjustment(bottomPen), width, 0);
		}
	}

	/**
	 *
	 */
	private void exportRightPen(
		JRPen topPen, 
		JRPen bottomPen, 
		JRPen rightPen, 
		int x, 
		int y, 
		int width, 
		int height
		) throws IOException
	{
		if (rightPen.getLineWidth().floatValue() > 0f) 
		{
			exportBorder(
				rightPen, 
				x + width, 
				y - topPen.getLineWidth().floatValue() / 2, 
				0, 
				height + (topPen.getLineWidth().floatValue() + bottomPen.getLineWidth().floatValue()) / 2
				);
			//exportBorder(rightPen, x + width - getAdjustment(rightPen), y, 0, height);
		}
	}


	protected void exportGenericElement(JRGenericPrintElement element)
	{
		GenericElementRtfHandler handler = (GenericElementRtfHandler) 
				GenericElementHandlerEnviroment.getHandler(
						element.getGenericType(), RTF_EXPORTER_KEY);
		
		if (handler != null)
		{
			handler.exportElement(exporterContext, element);
		}
		else
		{
			if (log.isDebugEnabled())
			{
				log.debug("No RTF generic element handler for " 
						+ element.getGenericType());
			}
		}
	}

	
	protected void exportHyperlink(JRPrintHyperlink link) throws IOException
	{
		String hlloc = null;
		String hlfr = null;
		String hlsrc = null;
		
		JRHyperlinkProducer customHandler = getCustomHandler(link);
		if (customHandler == null)
		{
			switch(link.getHyperlinkTypeValue())
			{
				case REFERENCE :
				{
					if (link.getHyperlinkReference() != null)
					{
						hlsrc = link.getHyperlinkReference();
						hlfr = hlsrc;
					}
					break;
				}
				case LOCAL_ANCHOR :
				{
					if (link.getHyperlinkAnchor() != null)
					{
						hlloc = link.getHyperlinkAnchor();
						hlfr = hlloc;
					}
					break;
				}
				case LOCAL_PAGE :
				{
					if (link.getHyperlinkPage() != null)
					{
						hlloc = JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + link.getHyperlinkPage().toString();
						hlfr = hlloc;
					}
					break;
				}
				case REMOTE_ANCHOR :
				{
					if (
						link.getHyperlinkReference() != null &&
						link.getHyperlinkAnchor() != null
						)
					{
						hlsrc = link.getHyperlinkReference() + "#" + link.getHyperlinkAnchor();
						hlfr = hlsrc;
					}
					break;
				}
				case REMOTE_PAGE :
				{
					if (
						link.getHyperlinkReference() != null &&
						link.getHyperlinkPage() != null
						)
					{
						hlsrc = link.getHyperlinkReference() + "#" + JR_PAGE_ANCHOR_PREFIX + "0_" + link.getHyperlinkPage().toString();
						hlfr = hlsrc;
					}
					break;
				}
				case NONE :
				default :
				{
					break;
				}
			}
		}
		else
		{
			hlsrc = customHandler.getHyperlink(link);
			hlfr = hlsrc;
		}

		if (hlfr != null)
		{
			writer.write("{\\sp{\\sn fIsButton}{\\sv 1}}");
			writer.write("{\\sp{\\sn pihlShape}{\\sv {\\*\\hl");
			writer.write("{\\hlfr ");
			writer.write(hlfr);
			writer.write(" }");
			if (hlloc != null)
			{
				writer.write("{\\hlloc ");
				writer.write(hlloc);
				writer.write(" }");
			}
			if (hlsrc != null)
			{
				writer.write("{\\hlsrc ");
				writer.write(hlsrc);
				writer.write(" }");
			}
			writer.write("}}}");
		}
	}


	protected void writeAnchor(String anchorName) throws IOException
	{
		writer.write("{\\*\\bkmkstart ");
		writer.write(anchorName);
		writer.write("}{\\*\\bkmkend ");
		writer.write(anchorName);
		writer.write("}");
	}

	private static float getXAlignFactor(JRPrintImage image)
	{
		float xalignFactor = 0f;
		switch (image.getHorizontalAlignmentValue())
		{
			case RIGHT :
			{
				xalignFactor = 1f;
				break;
			}
			case CENTER :
			{
				xalignFactor = 0.5f;
				break;
			}
			case LEFT :
			default :
			{
				xalignFactor = 0f;
				break;
			}
		}
		return xalignFactor;
	}

	private static float getYAlignFactor(JRPrintImage image)
	{
		float yalignFactor = 0f;
		switch (image.getVerticalAlignmentValue())
		{
			case BOTTOM :
			{
				yalignFactor = 1f;
				break;
			}
			case MIDDLE :
			{
				yalignFactor = 0.5f;
				break;
			}
			case TOP :
			default :
			{
				yalignFactor = 0f;
				break;
			}
		}
		return yalignFactor;
	}

	/**
	 *
	 */
	protected String getExporterKey()
	{
		return RTF_EXPORTER_KEY;
	}
	public static void main(String[] args) {
	    Pattern p = Pattern.compile("^<cust-li>(.*?)</cust-li>$");
        Matcher m = p.matcher("<cust-li>ala</cust-li>");
        m.find();
        System.out.println(m.group(1));
    }
}
