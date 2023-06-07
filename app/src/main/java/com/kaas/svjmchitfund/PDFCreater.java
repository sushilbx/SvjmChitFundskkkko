package com.kaas.svjmchitfund;

import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFilesImpl;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.HTML;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PDFCreater {
	public static String pdf_path = "";

	public boolean createPDF(String outputName, String outputFolderPath,
							 byte[] bytes, String imagesPath) {
		try {
			File dir = new File(outputFolderPath);
			if (!dir.exists())
				dir.mkdirs();

			File file = new File(dir, outputName);

			FileOutputStream fOut = new FileOutputStream(file);
			FontFactory.defaultEmbedding = true;
			Document document = new Document(PageSize.A4);
			document.addAuthor("Collegepond");
			document.addCreator("Collegepond");
			PdfWriter writer = PdfWriter.getInstance(document, fOut);
			writer.setPageCount(2);
			document.open();

			InputStream is = new ByteArrayInputStream(bytes);

			final TagProcessorFactory tagProcessorFactory = Tags
					.getHtmlTagProcessorFactory();
			tagProcessorFactory.removeProcessor(HTML.Tag.IMG);
			if (imagesPath != null) {
			/*	tagProcessorFactory.addProcessor(new ImageTagProcessor(
						imagesPath), HTML.Tag.IMG);*/
			}
			else {
				Constant.log("Pdf path " , "nnnnnnnnnnnn");
			}
			final CssFilesImpl cssFiles = new CssFilesImpl();
			cssFiles.add(XMLWorkerHelper.getInstance().getDefaultCSS());
			final StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(
					cssFiles);
			final HtmlPipelineContext hpc = new HtmlPipelineContext(
					new CssAppliersImpl(new XMLWorkerFontProvider()));
			hpc.setAcceptUnknown(true).autoBookmark(true)
					.setTagFactory(tagProcessorFactory);
			final HtmlPipeline htmlPipeline = new HtmlPipeline(hpc,
					new PdfWriterPipeline(document, writer));
			final Pipeline<?> pipeline = new CssResolverPipeline(cssResolver,
					htmlPipeline);
			final XMLWorker worker = new XMLWorker(pipeline, true);
			final Charset charset = Charset.forName("UTF-8");
			final XMLParser xmlParser = new XMLParser(true, worker, charset);

			// worker.parseXHtml(writer, document, new
			// StringReader("<p>helloworld</p>"));
			xmlParser.parse(is, charset);

			is.close();
			document.close();
			fOut.close();
			pdf_path = outputFolderPath + File.separator + outputName;
			Constant.log("Pdf path " , outputFolderPath + File.separator + outputName);


			return true;//outputFolderPath + File.separator + outputName;

		} catch (Exception e) {
			// Toast.makeText(ctx, ""+de, Toast.LENGTH_SHORT).show();
			Constant.log("Error", "Error Creating Pdf : " + e.toString());
			return false;//de.toString();
		}
	}

	public static void concatPDFs(List<InputStream> streamOfPDFFiles,
								  OutputStream outputStream, boolean paginate) {

		Document document = new Document();

		try {
			List<InputStream> pdfs = streamOfPDFFiles;
			List<PdfReader> readers = new ArrayList<PdfReader>();
			int totalPages = 0;
			Iterator<InputStream> iteratorPDFs = pdfs.iterator();

			// Create Readers for the pdfs.
			while (iteratorPDFs.hasNext()) {
				InputStream pdf = iteratorPDFs.next();
				PdfReader pdfReader = new PdfReader(pdf);
				readers.add(pdfReader);
				totalPages += pdfReader.getNumberOfPages();
			}

			// Create a writer for the outputstream
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
					BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

			// Holds the PDF data
			PdfContentByte cb = writer.getDirectContent();

			com.itextpdf.text.pdf.PdfImportedPage page;
			int currentPageNumber = 0;
			int pageOfCurrentReaderPDF = 0;
			Iterator<PdfReader> iteratorPDFReader = readers.iterator();

			// Loop through the PDF files and add to the output.
			while (iteratorPDFReader.hasNext()) {
				PdfReader pdfReader = iteratorPDFReader.next();

				// Create a new page in the target for each source page.
				while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
					document.newPage();
					pageOfCurrentReaderPDF++;
					currentPageNumber++;
					page = writer.getImportedPage(pdfReader,
							pageOfCurrentReaderPDF);
					cb.addTemplate(page, 0, 0);

					// Code for pagination.
					if (paginate) {
						cb.beginText();
						cb.setFontAndSize(bf, 9);
						cb.showTextAligned(PdfContentByte.ALIGN_CENTER, ""
										+ currentPageNumber + " of " + totalPages, 520,
								5, 0);
						cb.endText();
					}
				}
				pageOfCurrentReaderPDF = 0;

			}


			outputStream.flush();
			document.close();
			outputStream.close();

		} catch (Exception e) {
			System.err.println("Exception : " + e.getMessage());
		} finally {

			if (document.isOpen())
				document.close();

			try {
				if (outputStream != null)
					outputStream.close();
			} catch (IOException ioe) {
				System.err.println("Exception : " + ioe.getMessage());
			}
		}
	}
}

