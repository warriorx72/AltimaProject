package com.altima.springboot.app.view.pdf;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.altima.springboot.app.models.entity.ComercialCliente;
import com.altima.springboot.app.models.entity.ComercialCotizacionTotal;
import com.altima.springboot.app.models.entity.HrDireccion;
import com.altima.springboot.app.models.service.IEnviarCorreoService;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

@Component("/imprimir-cotizacion")
public class CotizacionesGenerarCotizacionPdfView extends AbstractPdfView{
	
	private static final String FILE1 = "src/main/resources/static/dist/pdf/cotizaciones/CV_Estatico.pdf";
	public static final String DEST = "src/main/resources/static/dist/pdf/cotizaciones/Resultado.pdf";

	@Autowired
    private IEnviarCorreoService enviarCorreoService;
	
	@SuppressWarnings("static-access")
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		/*
		 * 
		 * Variables del controller y de tiempo
		 * 
		 */
		document.setMargins(40f, 40f, 10f, 100f);
		
		@SuppressWarnings("unchecked")
		List<Object[]> prendas = (List<Object[]>) model.get("ListaCotizacionPrendas");
		ComercialCotizacionTotal cotitotal = (ComercialCotizacionTotal) model.get("cotizacionTotal");
		ComercialCliente cliente = (ComercialCliente) model.get("cliente");
		HrDireccion direccion = (HrDireccion) model.get("direccion");
		DateTime jodaTime = new DateTime();
		DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH-mm-ss-SSS");
		String nombrePDF = "Cotizacion_" + (String) model.get("id") + "_" + formatter.print(jodaTime) + ".pdf";
		String archivo = "src/main/resources/static/dist/pdf/cotizaciones/" + nombrePDF;
		String tipoCotizacion = (String) model.get("tipo");
		String correo = (String) model.get("mail");
		double iva = Double.parseDouble(cotitotal.getIva() );
		boolean totales = (boolean) model.get("totales");
		boolean cv = (boolean) model.get("cv");
		SimpleDateFormat formato = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
		String fecha = formato.format(new Date());

		
		/*
		 * 
		 * Colores y Fuente
		 * 
		 */
		Color fuerte = new Color(2, 136, 209);
		Color bajito = new Color(255, 137, 137);
		Color borderTable = new Color(205,205,205);
		Color colorDatos = new Color(170,170,170);
		Color TitulosBlancos = new Color(255,255,255);
		Color colorBorderBottom = new Color(255,185,24);
		Color borderGray = new Color(52,58,64);
		Color backgroundWhite = new Color(255,255,255);
		Color BackgroundTitle = new Color(90,90,90);
		Color textDarkGray = new Color(33,37,41);
		Font HelveticaBold = new Font(BaseFont.createFont( BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.EMBEDDED), 9);
		Font subtitulos = new Font(BaseFont.createFont( BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.EMBEDDED), 9, 0, textDarkGray);
		Font Titulos = new Font(BaseFont.createFont( BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED), 11, 0);
		Font TitulosOscuros = new Font(BaseFont.createFont( BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.EMBEDDED), 12, 0, textDarkGray);
		Font datosGris = new Font(BaseFont.createFont( BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.EMBEDDED), 9, 0, colorDatos);
		Font Helvetica = new Font(BaseFont.createFont( BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED), 9);
		Font letraCondiciones = new Font(BaseFont.createFont( BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED), 8);

		/*
		 * 
		 * Celda para espacios en blanco
		 * 
		 */
		PdfPTable espacio = new PdfPTable(1);
		PdfPCell cell = new PdfPCell(new Phrase(" "));
		cell.setBorder(0);
		espacio.addCell(cell);
		
		/**
		 * 
		 * 
		 * Cabezero del Documento
		 * 
		 * 
		 */
		
    	PdfPTable tablaNotaria = new PdfPTable(2);
    	tablaNotaria.setWidthPercentage(100);
    	PdfPCell numeroCotizacion = new PdfPCell(new Phrase("No. 25895", HelveticaBold));
    	PdfPCell lugarCotizacion = new PdfPCell(new Phrase("", TitulosOscuros));
    	numeroCotizacion.setBorder(0);
    	lugarCotizacion.setBorder(0);
		numeroCotizacion.setHorizontalAlignment(Element.ALIGN_RIGHT);
		lugarCotizacion.setHorizontalAlignment(Element.ALIGN_LEFT);
    	tablaNotaria.addCell(lugarCotizacion);
    	tablaNotaria.addCell(numeroCotizacion);
    	
		PdfPTable tableInfo =  new PdfPTable(3);
		tableInfo.setWidthPercentage(100);
		Image logo = Image.getInstance("src/main/resources/static/dist/img/logo.png");
		logo.scalePercent(25f);
		PdfPCell logoImg = new PdfPCell(new Phrase(""));
		PdfPCell tituloDocumento = new PdfPCell();
		
		tituloDocumento = new PdfPCell(new Phrase(model.get("TituloCotizacion").toString(), TitulosOscuros));
		
		PdfPCell fechaCotizacion2 = new PdfPCell(new Phrase(fecha.substring(0, 1).toUpperCase() + fecha.substring(1), Helvetica));
		logoImg.setPaddingBottom(10f);
		logoImg.setHorizontalAlignment(Element.ALIGN_LEFT);
		logoImg.setBorder(0);
		logoImg.setBorderWidthBottom(2f);
		logoImg.setBorderColor(borderGray);
		tituloDocumento.setHorizontalAlignment(Element.ALIGN_CENTER);
    	tituloDocumento.setVerticalAlignment(Element.ALIGN_CENTER);
		tituloDocumento.setBackgroundColor(backgroundWhite);
    	tituloDocumento.setBorder(0);
    	tituloDocumento.setBorder(Rectangle.BOTTOM);
    	tituloDocumento.setBorderWidthBottom(2f);
		tituloDocumento.setBorderColor(borderGray);
		tituloDocumento.setPaddingBottom(13f);
		fechaCotizacion2.setBorder(0);
		fechaCotizacion2.setBorderWidthBottom(2f);
		fechaCotizacion2.setPaddingBottom(13f);
		fechaCotizacion2.setBorderColor(borderGray);
		fechaCotizacion2.setHorizontalAlignment(Element.ALIGN_RIGHT);
		fechaCotizacion2.setVerticalAlignment(Element.ALIGN_CENTER);
		
		fechaCotizacion2.setPaddingTop(4f);
		tableInfo.addCell(logoImg);
		tableInfo.addCell(tituloDocumento);
		tableInfo.addCell(fechaCotizacion2);

		PdfPTable tablaHeader1 = new PdfPTable(1);
		tablaHeader1.setWidthPercentage(100);
		tablaHeader1.setSpacingAfter(8f);
    	PdfPCell celd = new PdfPCell(new Phrase("DATOS GENERALES DE LA EMPRESA", TitulosOscuros));
//    	celd.setHorizontalAlignment(Element.ALIGN_LEFT);
//    	celd.setVerticalAlignment(Element.ALIGN_CENTER);
//    	celd.setBackgroundColor(backgroundWhite);
//    	celd.setPaddingTop(-8f);
//		celd.setBorder(0);
//		tablaHeader1.addCell(celd);
		
		if(cliente.getApellidoPaterno()==null || cliente.getApellidoMaterno()==null) {
			celd = new PdfPCell(new Phrase(cliente.getNombre(), TitulosOscuros));
		}
		else {
			celd = new PdfPCell(new Phrase(cliente.getNombre()+" " +cliente.getApellidoPaterno()+" " +cliente.getApellidoMaterno(), TitulosOscuros));
		}
		
		celd.setHorizontalAlignment(Element.ALIGN_CENTER);
    	celd.setVerticalAlignment(Element.ALIGN_CENTER);
    	celd.setBackgroundColor(backgroundWhite);
		celd.setBorder(0);
		celd.setPaddingBottom(-4f);
		tablaHeader1.addCell(celd);
    	/*
    	 * 
    	 * Parte de la Direccion
    	 * 
    	 */
    	
    	PdfPTable tablaHeader2 = new PdfPTable(5);
    	tablaHeader2.setWidthPercentage(100);
    	PdfPCell celda;
    	
    	celda = new PdfPCell(new Phrase("Calle: ", Helvetica));
    	celda.setBorder(0);
    	celda.setHorizontalAlignment(Element.ALIGN_LEFT);
    	celda.setBorder(Rectangle.BOTTOM);
    	celda.setBorderColorBottom(borderTable);
    	celda.setBorderWidthBottom(2);
    	tablaHeader2.addCell(celda);
    	celda = new PdfPCell(new Phrase(direccion.getCalle(), datosGris));
    	celda.setBorder(0);
    	celda.setHorizontalAlignment(Element.ALIGN_LEFT);
    	celda.setBorder(Rectangle.BOTTOM);
    	celda.setBorderColorBottom(borderTable);
    	celda.setBorderWidthBottom(2);
    	tablaHeader2.addCell(celda);
    	celda = new PdfPCell(new Phrase(" "));
    	celda.setBorder(0);
    	tablaHeader2.addCell(celda);
    	celda = new PdfPCell(new Phrase("Número interior", Helvetica));
    	celda.setBorder(0);
    	celda.setHorizontalAlignment(Element.ALIGN_LEFT);
    	celda.setBorder(Rectangle.BOTTOM);
    	celda.setBorderColorBottom(borderTable);
    	celda.setBorderWidthBottom(2);
    	tablaHeader2.addCell(celda);
    	celda = new PdfPCell(new Phrase(direccion.getNumeroInt(), datosGris));
    	celda.setBorder(0);
    	celda.setHorizontalAlignment(Element.ALIGN_LEFT);
    	celda.setBorder(Rectangle.BOTTOM);
    	celda.setBorderColorBottom(borderTable);
    	celda.setBorderWidthBottom(2);
    	tablaHeader2.addCell(celda);
    	celda = new PdfPCell(new Phrase("Colonia: ", Helvetica));
    	celda.setBorder(0);
    	celda.setHorizontalAlignment(Element.ALIGN_LEFT);
    	celda.setBorder(Rectangle.BOTTOM);
    	celda.setBorderColorBottom(borderTable);
    	celda.setBorderWidthBottom(2);
    	tablaHeader2.addCell(celda);
    	celda = new PdfPCell(new Phrase(direccion.getColonia(), datosGris));
    	celda.setBorder(0);
    	celda.setHorizontalAlignment(Element.ALIGN_LEFT);
    	celda.setBorder(Rectangle.BOTTOM);
    	celda.setBorderColorBottom(borderTable);
    	celda.setBorderWidthBottom(2);
    	tablaHeader2.addCell(celda);
    	celda = new PdfPCell(new Phrase(" "));
    	celda.setBorder(0);
    	tablaHeader2.addCell(celda);
    	celda = new PdfPCell(new Phrase("CP:", Helvetica));
    	celda.setBorder(0);
    	celda.setHorizontalAlignment(Element.ALIGN_LEFT);
    	celda.setBorder(Rectangle.BOTTOM);
    	celda.setBorderColorBottom(borderTable);
    	celda.setBorderWidthBottom(2);
    	tablaHeader2.addCell(celda);
    	celda = new PdfPCell(new Phrase(direccion.getCodigoPostal(), datosGris));
    	celda.setBorder(0);
    	celda.setHorizontalAlignment(Element.ALIGN_LEFT);
    	celda.setBorder(Rectangle.BOTTOM);
    	celda.setBorderColorBottom(borderTable);
    	celda.setBorderWidthBottom(2);
    	tablaHeader2.addCell(celda);
    	celda = new PdfPCell(new Phrase("Delegación: ", Helvetica));
    	celda.setBorder(0);
    	celda.setHorizontalAlignment(Element.ALIGN_LEFT);
    	celda.setBorder(Rectangle.BOTTOM);
    	celda.setBorderColorBottom(borderTable);
    	celda.setBorderWidthBottom(2);
    	tablaHeader2.addCell(celda);
    	celda = new PdfPCell(new Phrase(direccion.getMunicipio(), datosGris));
    	celda.setBorder(0);
    	celda.setHorizontalAlignment(Element.ALIGN_LEFT);
    	celda.setBorder(Rectangle.BOTTOM);
    	celda.setBorderColorBottom(borderTable);
    	celda.setBorderWidthBottom(2);
    	tablaHeader2.addCell(celda);
    	celda = new PdfPCell(new Phrase(" "));
    	celda.setBorder(0);
    	tablaHeader2.addCell(celda);
    	celda = new PdfPCell(new Phrase("Estado", Helvetica));
    	celda.setBorder(0);
    	celda.setHorizontalAlignment(Element.ALIGN_LEFT);
    	celda.setBorder(Rectangle.BOTTOM);
    	celda.setBorderColorBottom(borderTable);
    	celda.setBorderWidthBottom(2);
    	tablaHeader2.addCell(celda);
    	celda = new PdfPCell(new Phrase(direccion.getEstado(), datosGris));
    	celda.setBorder(0);
    	celda.setHorizontalAlignment(Element.ALIGN_LEFT);
    	celda.setBorder(Rectangle.BOTTOM);
    	celda.setBorderColorBottom(borderTable); 
    	celda.setBorderWidthBottom(2);
    	tablaHeader2.addCell(celda);
    	celda = new PdfPCell(new Phrase("Correo: ", Helvetica));
    	celda.setBorder(0);
    	celda.setHorizontalAlignment(Element.ALIGN_LEFT);
    	celda.setBorder(Rectangle.BOTTOM);
    	celda.setBorderColorBottom(borderTable);
    	celda.setBorderWidthBottom(2);
    	tablaHeader2.addCell(celda);
    	celda = new PdfPCell(new Phrase(cliente.getCorreo(), datosGris));
    	celda.setBorder(0);
    	celda.setHorizontalAlignment(Element.ALIGN_LEFT);
    	celda.setBorder(Rectangle.BOTTOM);
    	celda.setBorderColorBottom(borderTable);
    	celda.setBorderWidthBottom(2);
    	tablaHeader2.addCell(celda);
    	celda = new PdfPCell(new Phrase(" "));
    	celda.setBorder(0);
    	tablaHeader2.addCell(celda);
    	celda = new PdfPCell(new Phrase("Teléfono: ", Helvetica));
    	celda.setBorder(0);
    	celda.setHorizontalAlignment(Element.ALIGN_LEFT);
    	celda.setBorder(Rectangle.BOTTOM);
    	celda.setBorderColorBottom(borderTable);
    	celda.setBorderWidthBottom(2);
    	tablaHeader2.addCell(celda);
    	celda = new PdfPCell(new Phrase(cliente.getTelefono(), datosGris));
    	celda.setBorder(0);
    	celda.setHorizontalAlignment(Element.ALIGN_LEFT);
    	celda.setBorder(Rectangle.BOTTOM);
    	celda.setBorderColorBottom(borderTable);
    	celda.setBorderWidthBottom(2);
    	tablaHeader2.addCell(celda);
    	celda = new PdfPCell(new Phrase(" "));
    	celda.setBorder(0);
    	tablaHeader2.addCell(celda);
    	celda = new PdfPCell(new Phrase(" ", Helvetica));
    	celda.setBorder(0);
    	celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
    	tablaHeader2.addCell(celda);
    	celda = new PdfPCell(new Phrase(" "));
    	celda.setBorder(0);
    	celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
    	tablaHeader2.addCell(celda);
    	try {
			tablaHeader2.setWidths(new float[] { 3f, 5f, 1f, 3f, 3f });
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

    	/*
    	 * 
    	 * Tabla para el nombre y por medio de la presente
    	 * 
    	 */
    	PdfPTable tablaDatosCotizacion = new PdfPTable(1);
		tablaDatosCotizacion.setWidthPercentage(100);
		tablaDatosCotizacion.setSpacingAfter(8f);
//    	PdfPCell tituloTabla = new PdfPCell(new Phrase("DATOS DE LA COTIZACIÓN", TitulosOscuros));
//    	tituloTabla.setHorizontalAlignment(Element.ALIGN_LEFT);
//		tituloTabla.setVerticalAlignment(Element.ALIGN_CENTER);
//		tituloTabla.setBorder(0);
//		tablaDatosCotizacion.addCell(tituloTabla);


		PdfPTable tablaHeader3 = new PdfPTable(1);
    	tablaHeader3.setWidthPercentage(100);
    	PdfPCell nombre = new PdfPCell(new Phrase("At'n. "+cliente.getNombreContacto(), HelveticaBold));
    	PdfPCell leyenda = new PdfPCell(new Phrase("Por medio de la presente nos permitimos poner a su consideración la cotización de las siguientes prendas: ", Helvetica));
    	nombre.setBorder(0);
    	leyenda.setBorder(0);
    	leyenda.setPaddingBottom(-5f);
    	nombre.setHorizontalAlignment(Element.ALIGN_CENTER);
    	leyenda.setHorizontalAlignment(Element.ALIGN_LEFT);
    	tablaHeader3.addCell(nombre);
    	tablaHeader3.addCell(leyenda);
		
		/**
		 * 
		 * 
		 * Cuerpo del documento 
		 * 
		 * 
		 */
		PdfPTable tablaPrendas = null;
		//Si pasa aqui, es 1, es decir una cotizacion General
		if(tipoCotizacion.equalsIgnoreCase("1")) {
			tablaPrendas = new PdfPTable(4);
			tablaPrendas.setWidthPercentage(100);
			tablaPrendas.setWidths(new float[] { 8f, 8f, 4f, 4f });
			PdfPCell vacio = new PdfPCell(new Phrase(""));
			vacio.setBorder(0);
			vacio.setBorderWidthBottom(2f);
			vacio.setBorderColorBottom(borderGray);
			PdfPCell nombrePrendaCabezero = new PdfPCell(new Phrase("Prenda", HelveticaBold));
			PdfPCell nombrePrendaCabezero2 = new PdfPCell(new Phrase("Composición", HelveticaBold));
			PdfPCell precioPrendaCabezero = new PdfPCell(new Phrase("Precio", HelveticaBold));
			nombrePrendaCabezero.setBorderColor(borderGray);
			nombrePrendaCabezero.setBorder(0);
			nombrePrendaCabezero.setBorderWidthBottom(2f);
			nombrePrendaCabezero.setPaddingBottom(8f);
			nombrePrendaCabezero.setPaddingTop(6f);
			nombrePrendaCabezero.setHorizontalAlignment(Element.ALIGN_CENTER);
			nombrePrendaCabezero.setVerticalAlignment(Element.ALIGN_CENTER);
			nombrePrendaCabezero2.setBorderColor(borderGray);
			nombrePrendaCabezero2.setBorder(0);
			nombrePrendaCabezero2.setBorderWidthBottom(2f);
			nombrePrendaCabezero2.setPaddingBottom(8f);
			nombrePrendaCabezero2.setPaddingTop(6f);
			nombrePrendaCabezero2.setHorizontalAlignment(Element.ALIGN_CENTER);
			nombrePrendaCabezero2.setVerticalAlignment(Element.ALIGN_CENTER);
			precioPrendaCabezero.setBorderColor(borderGray);
			precioPrendaCabezero.setBorder(0);
			precioPrendaCabezero.setBorderWidthBottom(2f);
			precioPrendaCabezero.setPaddingBottom(8f);
			precioPrendaCabezero.setPaddingTop(6f);
			precioPrendaCabezero.setHorizontalAlignment(Element.ALIGN_CENTER);
			precioPrendaCabezero.setVerticalAlignment(Element.ALIGN_CENTER);
			
			
			
			tablaPrendas.addCell(nombrePrendaCabezero);
			tablaPrendas.addCell(nombrePrendaCabezero2);
			tablaPrendas.addCell(vacio);
			tablaPrendas.addCell(precioPrendaCabezero);
			float Total = 0;
			for(int con = 0; con < prendas.size(); con++) {
				Object[] aux = (Object[]) prendas.get(con);
				Total += Float.valueOf(aux[19].toString());
				PdfPCell nombrePrenda = new PdfPCell(new Phrase(aux[2].toString(), Helvetica));
				PdfPCell nombreTela = new PdfPCell(new Phrase(aux[7].toString(), Helvetica));
				PdfPCell precioPrenda = new PdfPCell(new Phrase(aux[17].toString()+"0", HelveticaBold));
//				nombrePrenda.setPadding(3f);
//				precioPrenda.setPadding(3f);
				vacio.setBorderColorBottom(borderTable);
				nombrePrenda.setBorderColorBottom(borderTable);
				nombrePrenda.setBorderWidthBottom(2);
				nombrePrenda.setBorder(Rectangle.BOTTOM);
				nombrePrenda.setPaddingBottom(12f);
				nombrePrenda.setHorizontalAlignment(Element.ALIGN_CENTER);
				nombrePrenda.setVerticalAlignment(Element.ALIGN_CENTER);
				nombreTela.setBorderColorBottom(borderTable);
				nombreTela.setBorderWidthBottom(2);
				nombreTela.setBorder(Rectangle.BOTTOM);
				nombreTela.setPaddingBottom(12f);
				nombreTela.setHorizontalAlignment(Element.ALIGN_CENTER);
				nombreTela.setVerticalAlignment(Element.ALIGN_CENTER);
				precioPrenda.setBorderColorBottom(borderTable);
				precioPrenda.setBorderWidthBottom(2);
				precioPrenda.setBorder(Rectangle.BOTTOM);
				precioPrenda.setPaddingBottom(12f);
				precioPrenda.setHorizontalAlignment(Element.ALIGN_CENTER);
				precioPrenda.setVerticalAlignment(Element.ALIGN_CENTER);
				tablaPrendas.addCell(nombrePrenda);
				tablaPrendas.addCell(nombreTela);
				tablaPrendas.addCell(vacio);
				tablaPrendas.addCell(precioPrenda);
			}
			
			//Si tiene los totales se le anexan
			if(totales) {
				
				PdfPCell totalTitulo = new PdfPCell(new Phrase("Total", HelveticaBold));
				PdfPCell totalNumero = new PdfPCell(new Phrase("" + Total+"0", HelveticaBold));
//				totalTitulo.setPadding(3f);
//				totalNumero.setPadding(3f);
				vacio.setBorder(0);
				totalTitulo.setPaddingBottom(18f);
				totalTitulo.setBorder(0); 
				totalTitulo.setBorder(Rectangle.BOTTOM);
				totalTitulo.setBorderColorBottom(borderGray);
				totalTitulo.setBorderWidthBottom(2);
				totalTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
				totalNumero.setBorder(Rectangle.BOTTOM);
				totalNumero.setBorderColorBottom(borderGray);
				totalNumero.setBorderWidthBottom(2);
				totalNumero.setHorizontalAlignment(Element.ALIGN_CENTER);
				tablaPrendas.addCell(vacio);
				tablaPrendas.addCell(vacio);
				tablaPrendas.addCell(totalTitulo);
				tablaPrendas.addCell(totalNumero);
			}
		}
		//Si pasa aqui es porque es una desglosada por tipo de prenda xd
		else if(tipoCotizacion.equalsIgnoreCase("2")) {
			//Se declara la tabla
			tablaPrendas = new PdfPTable(1);
			tablaPrendas.setWidthPercentage(100);
			
			PdfPCell vacia = new PdfPCell(new Phrase(" "));
			vacia.setBorder(0);
			vacia.setPadding(3f);
			PdfPTable bloques = new PdfPTable(7);
			bloques.setWidths(new float[] { 2.8f, 4f, 3f, 3f, 5f, 2.2f, 2.5f });
			bloques.setWidthPercentage(100);
			float Subtotal = 0;
			float IVA = 0;
			float Total = 0;
			float descuentoCargo = 0;
			int contadorCoord = 1;
			float SubtotalporCoord = 0;
			for(int con = 0; con < prendas.size(); con++) {
				Object[] aux = (Object[]) prendas.get(con);
				
				if(contadorCoord==Integer.parseInt(aux[9].toString())) {
					if(SubtotalporCoord!=0) {
						PdfPCell TotalLetra = new PdfPCell(new Phrase("Total:", subtitulos));
						PdfPCell cuerpoCoord = new PdfPCell(new Phrase("$" + SubtotalporCoord+"0", HelveticaBold));
						cuerpoCoord.setBorderColorBottom(borderTable);
						cuerpoCoord.setBorderWidthBottom(2);
						cuerpoCoord.setBorder(Rectangle.BOTTOM);
						cuerpoCoord.setPaddingBottom(10f);
						cuerpoCoord.setPaddingTop(8f);
						TotalLetra.setPaddingBottom(10f);
						TotalLetra.setPaddingTop(8f);
						TotalLetra.setBorder(0); 
						TotalLetra.setBorder(Rectangle.BOTTOM);
						TotalLetra.setBorderColorBottom(borderTable);
						TotalLetra.setBorderWidthBottom(2);
						bloques.addCell(vacia);
						bloques.addCell(vacia);
						bloques.addCell(vacia);
						bloques.addCell(vacia);
						bloques.addCell(vacia);
						bloques.addCell(TotalLetra);
						bloques.addCell(cuerpoCoord);
						SubtotalporCoord=0;
						
						PdfPCell cuerpoBloque = new PdfPCell(bloques);
						cuerpoBloque.setBorder(0);
						tablaPrendas.addCell(cuerpoBloque);
					}
					
					bloques = new PdfPTable(7);
					bloques.setWidths(new float[] { 2.8f, 4f, 3f, 3f, 5f, 2.2f, 2.5f });
					bloques.setWidthPercentage(100);
					
					PdfPCell cuerpoCoord = new PdfPCell(new Phrase("Coordinado " + aux[9].toString(), HelveticaBold));
					PdfPCell vacio = new PdfPCell();
					cuerpoCoord.setBorder(0);
					cuerpoCoord.setPaddingBottom(10f);
					cuerpoCoord.setPaddingTop(8f);
					vacio.setBorder(0);
					vacio.setPaddingBottom(10f);
					vacio.setPaddingTop(8f);
					bloques.addCell(cuerpoCoord);
					bloques.addCell(vacio);
					bloques.addCell(vacio);
					bloques.addCell(vacio);
					bloques.addCell(vacio);
					bloques.addCell(vacio);
					bloques.addCell(vacio);
					
					PdfPCell Cabezero1 = new PdfPCell(new Phrase("Cantidad", subtitulos));
					PdfPCell Cabezero2 = new PdfPCell(new Phrase("Modelo", subtitulos));
					PdfPCell Cabezero3 = new PdfPCell(new Phrase("Tela", subtitulos));
					PdfPCell Cabezero4 = new PdfPCell(new Phrase("Color", subtitulos));
					PdfPCell Cabezero5 = new PdfPCell(new Phrase("Familia de Composición", subtitulos));
					PdfPCell Cabezero6 = new PdfPCell(new Phrase("Pre. Unitario", subtitulos));
					PdfPCell Cabezero7 = new PdfPCell(new Phrase("Total", subtitulos));
					Cabezero1.setBorder(0);
					Cabezero1.setPaddingBottom(8f);
					Cabezero1.setPaddingTop(6f);
					Cabezero1.setVerticalAlignment(Element.ALIGN_CENTER);
					Cabezero1.setBorderWidthBottom(2f);
					Cabezero1.setBorderColor(borderGray);
					Cabezero2.setBorder(0);
					Cabezero2.setPaddingBottom(8f);
					Cabezero2.setPaddingTop(6f);
					Cabezero2.setVerticalAlignment(Element.ALIGN_CENTER);
					Cabezero2.setBorderWidthBottom(2f);
					Cabezero2.setBorderColor(borderGray);
					Cabezero3.setBorder(0);
					Cabezero3.setPaddingBottom(8f);
					Cabezero3.setPaddingTop(6f);
					Cabezero3.setVerticalAlignment(Element.ALIGN_CENTER);
					Cabezero3.setBorderWidthBottom(2f);
					Cabezero3.setBorderColor(borderGray);
					Cabezero4.setBorder(0);
					Cabezero4.setPaddingBottom(8f);
					Cabezero4.setPaddingTop(6f);
					Cabezero4.setVerticalAlignment(Element.ALIGN_CENTER);
					Cabezero4.setBorderWidthBottom(2f);
					Cabezero4.setBorderColor(borderGray);
					Cabezero5.setBorder(0);
					Cabezero5.setPaddingBottom(8f);
					Cabezero5.setPaddingTop(6f);
					Cabezero5.setVerticalAlignment(Element.ALIGN_CENTER);
					Cabezero5.setBorderWidthBottom(2f);
					Cabezero5.setBorderColor(borderGray);
					Cabezero6.setBorder(0);
					Cabezero6.setPaddingBottom(8f);
					Cabezero6.setPaddingTop(6f);
					Cabezero6.setVerticalAlignment(Element.ALIGN_CENTER);
					Cabezero6.setBorderWidthBottom(2f);
					Cabezero6.setBorderColor(borderGray);
					Cabezero7.setBorder(0);
					Cabezero7.setPaddingBottom(8f);
					Cabezero7.setPaddingTop(6f);
					Cabezero7.setVerticalAlignment(Element.ALIGN_CENTER);
					Cabezero7.setBorderWidthBottom(2f);
					Cabezero7.setBorderColor(borderGray);
					bloques.addCell(Cabezero1);
					bloques.addCell(Cabezero2);
					bloques.addCell(Cabezero3);
					bloques.addCell(Cabezero4);
					bloques.addCell(Cabezero5);
					bloques.addCell(Cabezero6);
					bloques.addCell(Cabezero7);
					
					contadorCoord++;
					
				}
				SubtotalporCoord += Float.valueOf(aux[27].toString());
				
				Subtotal += Float.valueOf(aux[27].toString());
				PdfPCell cuerpo1 = new PdfPCell(new Phrase(aux[10].toString(), Helvetica));
				PdfPCell cuerpo2 = new PdfPCell(new Phrase(aux[4].toString(), Helvetica));
				PdfPCell cuerpo3 = new PdfPCell();
				PdfPCell cuerpo4 = new PdfPCell();
				PdfPCell cuerpo5 = new PdfPCell();
				try {
					cuerpo3 = new PdfPCell(new Phrase(aux[6].toString(), Helvetica));
					cuerpo4 = new PdfPCell(new Phrase(aux[8].toString(), Helvetica));
					cuerpo5 = new PdfPCell(new Phrase("Por definir", Helvetica));
				}
				catch(Exception e){
					cuerpo3 = new PdfPCell(new Phrase("Por definir", Helvetica));
					cuerpo4 = new PdfPCell(new Phrase("Por definir", Helvetica));
					cuerpo5 = new PdfPCell(new Phrase(aux[7].toString(), Helvetica));
				}
				PdfPCell cuerpo6 = new PdfPCell(new Phrase("$" + aux[26].toString()+"0", Helvetica));
				PdfPCell cuerpo7 = new PdfPCell(new Phrase("$" + aux[27].toString()+"0", HelveticaBold));
				cuerpo1.setBorderColorBottom(borderTable);
				cuerpo1.setBorderWidthBottom(2);
				cuerpo1.setBorder(Rectangle.BOTTOM);
				cuerpo1.setPaddingBottom(10f);
				cuerpo1.setPaddingTop(8f);
				cuerpo2.setBorderColorBottom(borderTable);
				cuerpo2.setBorderWidthBottom(2);
				cuerpo2.setBorder(Rectangle.BOTTOM);
				cuerpo2.setPaddingBottom(10f);
				cuerpo2.setPaddingTop(8f);
				cuerpo3.setBorderColorBottom(borderTable);
				cuerpo3.setBorderWidthBottom(2);
				cuerpo3.setBorder(Rectangle.BOTTOM);
				cuerpo3.setPaddingBottom(10f);
				cuerpo3.setPaddingTop(8f);
				cuerpo4.setBorderColorBottom(borderTable);
				cuerpo4.setBorderWidthBottom(2);
				cuerpo4.setBorder(Rectangle.BOTTOM);
				cuerpo4.setPaddingBottom(10f);
				cuerpo4.setPaddingTop(8f);
				cuerpo5.setBorderColorBottom(borderTable);
				cuerpo5.setBorderWidthBottom(2);
				cuerpo5.setBorder(Rectangle.BOTTOM);
				cuerpo5.setPaddingBottom(10f);
				cuerpo5.setPaddingTop(8f);
				cuerpo6.setBorderColorBottom(borderTable);
				cuerpo6.setBorderWidthBottom(2);
				cuerpo6.setBorder(Rectangle.BOTTOM);
				cuerpo6.setPaddingBottom(10f);
				cuerpo6.setPaddingTop(8f);
				cuerpo7.setBorderColorBottom(borderTable);
				cuerpo7.setBorderWidthBottom(2);
				cuerpo7.setBorder(Rectangle.BOTTOM);
				cuerpo7.setPaddingBottom(10f);
				cuerpo7.setPaddingTop(8f);
				bloques.addCell(cuerpo1);
				bloques.addCell(cuerpo2);
				bloques.addCell(cuerpo3);
				bloques.addCell(cuerpo4);
				bloques.addCell(cuerpo5);
				bloques.addCell(cuerpo6);
				bloques.addCell(cuerpo7);
				
			}
			descuentoCargo = Float.parseFloat(cotitotal.getDescuentoMonto());
			IVA = (int)( Subtotal * ( iva/100.0f ));
			Total = (IVA + Subtotal + descuentoCargo);
			
			//Si tiene los totales se le anexan
			if(totales) {
				
			
				PdfPCell subTotalLetra = new PdfPCell(new Phrase("Subtotal:", subtitulos));
				PdfPCell subTotalNumero = new PdfPCell(new Phrase("$" + Subtotal+"0", HelveticaBold));
				PdfPCell descuentoLetra = new PdfPCell(new Phrase("Descuento/ Cargo", subtitulos));
				PdfPCell descuentoNumero = new PdfPCell(new Phrase("$" + descuentoCargo+"0", HelveticaBold));
				PdfPCell ivaLetra = new PdfPCell(new Phrase("I.V.A: ", subtitulos));
				PdfPCell ivaNumero = new PdfPCell(new Phrase("$" + IVA+"0", HelveticaBold));
				PdfPCell TotalLetra = new PdfPCell(new Phrase("Total:", subtitulos));
				PdfPCell TotalNumero = new PdfPCell(new Phrase("$" + Total+"0", HelveticaBold));
				PdfPCell cuerpoCoord = new PdfPCell(new Phrase("$" + SubtotalporCoord+"0", HelveticaBold));
				cuerpoCoord.setBorderColorBottom(borderTable);
				cuerpoCoord.setBorderWidthBottom(2);
				cuerpoCoord.setBorder(Rectangle.BOTTOM);
				cuerpoCoord.setPaddingBottom(10f);
				cuerpoCoord.setPaddingTop(8f);
				
				subTotalLetra.setPaddingBottom(10f);
				subTotalLetra.setPaddingTop(8f);
				subTotalLetra.setBorder(0);
				subTotalLetra.setBorder(Rectangle.BOTTOM);
				subTotalLetra.setBorderColorBottom(borderGray);
				subTotalLetra.setBorderWidthBottom(2);
				subTotalNumero.setPaddingBottom(10f);
				subTotalNumero.setPaddingTop(8f);
				subTotalNumero.setBorder(Rectangle.BOTTOM);
				subTotalNumero.setBorderColorBottom(borderGray);
				subTotalNumero.setBorderWidthBottom(2);
				descuentoLetra.setPaddingBottom(10f);
				descuentoLetra.setPaddingTop(8f);
				descuentoLetra.setBorder(0);
				descuentoLetra.setBorder(Rectangle.BOTTOM);
				descuentoLetra.setBorderColorBottom(borderGray);
				descuentoLetra.setBorderWidthBottom(2);
				descuentoNumero.setPaddingBottom(10f);
				descuentoNumero.setPaddingTop(8f);
				descuentoNumero.setBorder(Rectangle.BOTTOM);
				descuentoNumero.setBorderColorBottom(borderGray);
				descuentoNumero.setBorderWidthBottom(2);
				ivaLetra.setPaddingBottom(10f);
				ivaLetra.setPaddingTop(8f);
				ivaLetra.setBorder(0);
				ivaLetra.setBorder(Rectangle.BOTTOM);
				ivaLetra.setBorderColorBottom(borderGray);
				ivaLetra.setBorderWidthBottom(2);
				ivaNumero.setPaddingBottom(10f);
				ivaNumero.setPaddingTop(8f);
				ivaNumero.setBorder(Rectangle.BOTTOM);
				ivaNumero.setBorderColorBottom(borderGray);
				ivaNumero.setBorderWidthBottom(2);
				TotalLetra.setPaddingBottom(10f);
				TotalLetra.setPaddingTop(8f);
				TotalLetra.setBorder(0); 
				TotalLetra.setBorder(Rectangle.BOTTOM);
				TotalLetra.setBorderColorBottom(borderGray);
				TotalLetra.setBorderWidthBottom(2);
				TotalNumero.setBorder(Rectangle.BOTTOM);
				TotalNumero.setBorderColorBottom(borderGray);
				TotalNumero.setBorderWidthBottom(2);
				TotalNumero.setPaddingBottom(10f);
				TotalNumero.setPaddingTop(8f);
				
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				TotalLetra.setBorderColorBottom(borderTable);
				bloques.addCell(TotalLetra);
				bloques.addCell(cuerpoCoord);
				
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(subTotalLetra);
				bloques.addCell(subTotalNumero);
				
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(descuentoLetra);
				bloques.addCell(descuentoNumero);
				
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(ivaLetra);
				bloques.addCell(ivaNumero);
				
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				TotalLetra.setBorderColorBottom(borderGray);
				bloques.addCell(TotalLetra);
				bloques.addCell(TotalNumero);
				
				
			}
			PdfPCell cuerpoBloque = new PdfPCell(bloques);
			cuerpoBloque.setBorder(0);
			tablaPrendas.addCell(cuerpoBloque);
		}
		else {
			tablaPrendas = new PdfPTable(1);
			tablaPrendas.setWidthPercentage(100);
			
			PdfPTable bloques = new PdfPTable(5);
			bloques.setWidths(new float[] { 2.8f, 5f, 6f, 2.2f, 2.5f });
			bloques.setWidthPercentage(100);
			PdfPCell vacia = new PdfPCell(new Phrase(" "));
			vacia.setBorder(0);
			vacia.setPadding(3f);
			
			float Subtotal = 0;
			float descuentoCargo = 0;
			float IVA = 0;
			float Total = 0;
			int contadorCoord=1;
			float SubtotalporCoord = 0;
			for(int con = 0; con < prendas.size(); con++) {
				Object[] aux = (Object[]) prendas.get(con);
				
				if(contadorCoord==Integer.parseInt(aux[9].toString())) {
					if(SubtotalporCoord!=0) {
						PdfPCell TotalLetra = new PdfPCell(new Phrase("Total:", subtitulos));
						PdfPCell cuerpoCoord = new PdfPCell(new Phrase("$" + SubtotalporCoord+"0", HelveticaBold));
						cuerpoCoord.setBorderColorBottom(borderTable);
						cuerpoCoord.setBorderWidthBottom(2);
						cuerpoCoord.setBorder(Rectangle.BOTTOM);
						cuerpoCoord.setPaddingBottom(10f);
						cuerpoCoord.setPaddingTop(8f);
						TotalLetra.setPaddingBottom(10f);
						TotalLetra.setPaddingTop(8f);
						TotalLetra.setBorder(0); 
						TotalLetra.setBorder(Rectangle.BOTTOM);
						TotalLetra.setBorderColorBottom(borderTable);
						TotalLetra.setBorderWidthBottom(2);
						bloques.addCell(vacia);
						bloques.addCell(vacia);
						bloques.addCell(vacia);
						bloques.addCell(TotalLetra);
						bloques.addCell(cuerpoCoord);
						SubtotalporCoord=0;
						
						PdfPCell cuerpoBloque = new PdfPCell(bloques);
						cuerpoBloque.setBorder(0);
						tablaPrendas.addCell(cuerpoBloque);
					}
					
					bloques = new PdfPTable(5);
					bloques.setWidths(new float[] { 2.8f, 5f, 6f, 2.2f, 2.5f });
					bloques.setWidthPercentage(100);
					
					PdfPCell cuerpoCoord = new PdfPCell(new Phrase("Coordinado " + aux[9].toString(), HelveticaBold));
					PdfPCell vacio = new PdfPCell();
					cuerpoCoord.setBorder(0);
					cuerpoCoord.setPaddingBottom(10f);
					cuerpoCoord.setPaddingTop(8f);
					vacio.setBorder(0);
					vacio.setPaddingBottom(10f);
					vacio.setPaddingTop(8f);
					bloques.addCell(cuerpoCoord);
					bloques.addCell(vacio);
					bloques.addCell(vacio);
					bloques.addCell(vacio);
					bloques.addCell(vacio);
					
					PdfPCell Cabezero1 = new PdfPCell(new Phrase("Cantidad", subtitulos));
					PdfPCell Cabezero2 = new PdfPCell(new Phrase("Prenda", subtitulos));
					PdfPCell Cabezero3 = new PdfPCell(new Phrase("Familia de Composición", subtitulos));
					PdfPCell Cabezero4 = new PdfPCell(new Phrase("Pre. Unitario", subtitulos));
					PdfPCell Cabezero5 = new PdfPCell(new Phrase("Total", subtitulos));
					Cabezero1.setBorder(0);
					Cabezero1.setPaddingBottom(8f);
					Cabezero1.setPaddingTop(6f);
					Cabezero1.setVerticalAlignment(Element.ALIGN_CENTER);
					Cabezero1.setBorderWidthBottom(2f);
					Cabezero1.setBorderColor(borderGray);
					Cabezero2.setBorder(0);
					Cabezero2.setPaddingBottom(8f);
					Cabezero2.setPaddingTop(6f);
					Cabezero2.setVerticalAlignment(Element.ALIGN_CENTER);
					Cabezero2.setBorderWidthBottom(2f);
					Cabezero2.setBorderColor(borderGray);
					Cabezero3.setBorder(0);
					Cabezero3.setPaddingBottom(8f);
					Cabezero3.setPaddingTop(6f);
					Cabezero3.setVerticalAlignment(Element.ALIGN_CENTER);
					Cabezero3.setBorderWidthBottom(2f);
					Cabezero3.setBorderColor(borderGray);
					Cabezero4.setBorder(0);
					Cabezero4.setPaddingBottom(8f);
					Cabezero4.setPaddingTop(6f);
					Cabezero4.setVerticalAlignment(Element.ALIGN_CENTER);
					Cabezero4.setBorderWidthBottom(2f);
					Cabezero4.setBorderColor(borderGray);
					Cabezero5.setBorder(0);
					Cabezero5.setPaddingBottom(8f);
					Cabezero5.setPaddingTop(6f);
					Cabezero5.setVerticalAlignment(Element.ALIGN_CENTER);
					Cabezero5.setBorderWidthBottom(2f);
					Cabezero5.setBorderColor(borderGray);
					bloques.addCell(Cabezero1);
					bloques.addCell(Cabezero2);
					bloques.addCell(Cabezero3);
					bloques.addCell(Cabezero4);
					bloques.addCell(Cabezero5);
					
					
					contadorCoord++;
					
				}
				SubtotalporCoord += Float.valueOf(aux[27].toString());
				Subtotal += Float.valueOf(aux[27].toString());
				PdfPCell cuerpo1 = new PdfPCell(new Phrase(aux[10].toString(), Helvetica));
				PdfPCell cuerpo2 = new PdfPCell(new Phrase(aux[2].toString(), Helvetica));
				PdfPCell cuerpo3 = new PdfPCell(new Phrase(aux[7].toString(), Helvetica));
				PdfPCell cuerpo4 = new PdfPCell(new Phrase("$" + aux[26].toString()+"0", Helvetica));
				PdfPCell cuerpo5 = new PdfPCell(new Phrase("$" + aux[27].toString()+"0", HelveticaBold));
				cuerpo1.setBorderColorBottom(borderTable);
				cuerpo1.setBorderWidthBottom(2);
				cuerpo1.setBorder(Rectangle.BOTTOM);
				cuerpo1.setHorizontalAlignment(Element.ALIGN_CENTER);
				cuerpo1.setPaddingBottom(10f);
				cuerpo1.setPaddingTop(8f);
				cuerpo2.setBorderColorBottom(borderTable);
				cuerpo2.setBorderWidthBottom(2);
				cuerpo2.setBorder(Rectangle.BOTTOM);
				cuerpo2.setPaddingBottom(10f);
				cuerpo2.setPaddingTop(8f);
				cuerpo3.setBorderColorBottom(borderTable);
				cuerpo3.setBorderWidthBottom(2);
				cuerpo3.setBorder(Rectangle.BOTTOM);
				cuerpo3.setPaddingBottom(10f);
				cuerpo3.setPaddingTop(8f);
				cuerpo4.setBorderColorBottom(borderTable);
				cuerpo4.setBorderWidthBottom(2);
				cuerpo4.setBorder(Rectangle.BOTTOM);
				cuerpo4.setPaddingBottom(10f);
				cuerpo4.setPaddingTop(8f);
				cuerpo5.setBorderColorBottom(borderTable);
				cuerpo5.setBorderWidthBottom(2);
				cuerpo5.setBorder(Rectangle.BOTTOM);
				cuerpo5.setPaddingBottom(10f);
				cuerpo5.setPaddingTop(8f);
				bloques.addCell(cuerpo1);
				bloques.addCell(cuerpo2);
				bloques.addCell(cuerpo3);
				bloques.addCell(cuerpo4);
				bloques.addCell(cuerpo5);
			}
			descuentoCargo = Float.parseFloat(cotitotal.getDescuentoMonto());
			IVA = (int)( Subtotal * ( iva/100.0f ));
			Total = (IVA + Subtotal + descuentoCargo);
					
			//Si tiene los totales se le anexan
			if(totales) {
				
				
				PdfPCell subTotalLetra = new PdfPCell(new Phrase("Subtotal:", subtitulos));
				PdfPCell subTotalNumero = new PdfPCell(new Phrase("$" + Subtotal+"0", HelveticaBold));
				PdfPCell descuentoLetra = new PdfPCell(new Phrase("Descuento/ Cargo", subtitulos));
				PdfPCell descuentoNumero = new PdfPCell(new Phrase("$" + descuentoCargo+"0", HelveticaBold));
				PdfPCell ivaLetra = new PdfPCell(new Phrase("I.V.A: ", subtitulos));
				PdfPCell ivaNumero = new PdfPCell(new Phrase("$" + IVA+"0", HelveticaBold));
				PdfPCell TotalLetra = new PdfPCell(new Phrase("Total:", subtitulos));
				PdfPCell TotalNumero = new PdfPCell(new Phrase("$" + Total+"0", HelveticaBold));
				PdfPCell cuerpoCoord = new PdfPCell(new Phrase("$" + SubtotalporCoord+"0", HelveticaBold));
				cuerpoCoord.setBorderColorBottom(borderTable);
				cuerpoCoord.setBorderWidthBottom(2);
				cuerpoCoord.setBorder(Rectangle.BOTTOM);
				cuerpoCoord.setPaddingBottom(10f);
				cuerpoCoord.setPaddingTop(8f);

				subTotalLetra.setPaddingBottom(10f);
				subTotalLetra.setPaddingTop(8f);
				subTotalLetra.setBorder(0);
				subTotalLetra.setBorder(Rectangle.BOTTOM);
				subTotalLetra.setBorderColorBottom(borderGray);
				subTotalLetra.setBorderWidthBottom(2);
				subTotalNumero.setPaddingBottom(10f);
				subTotalNumero.setPaddingTop(8f);
				subTotalNumero.setBorder(Rectangle.BOTTOM);
				subTotalNumero.setBorderColorBottom(borderGray);
				subTotalNumero.setBorderWidthBottom(2);
				descuentoLetra.setPaddingBottom(10f);
				descuentoLetra.setPaddingTop(8f);
				descuentoLetra.setBorder(0);
				descuentoLetra.setBorder(Rectangle.BOTTOM);
				descuentoLetra.setBorderColorBottom(borderGray);
				descuentoLetra.setBorderWidthBottom(2);
				descuentoNumero.setPaddingBottom(10f);
				descuentoNumero.setPaddingTop(8f);
				descuentoNumero.setBorder(Rectangle.BOTTOM);
				descuentoNumero.setBorderColorBottom(borderGray);
				descuentoNumero.setBorderWidthBottom(2);
				ivaLetra.setPaddingBottom(10f);
				ivaLetra.setPaddingTop(8f);
				ivaLetra.setBorder(0);
				ivaLetra.setBorder(Rectangle.BOTTOM);
				ivaLetra.setBorderColorBottom(borderGray);
				ivaLetra.setBorderWidthBottom(2);
				ivaNumero.setPaddingBottom(10f);
				ivaNumero.setPaddingTop(8f);
				ivaNumero.setBorder(Rectangle.BOTTOM);
				ivaNumero.setBorderColorBottom(borderGray);
				ivaNumero.setBorderWidthBottom(2);
				TotalLetra.setPaddingBottom(10f);
				TotalLetra.setPaddingTop(8f);
				TotalLetra.setBorder(0); 
				TotalLetra.setBorder(Rectangle.BOTTOM);
				TotalLetra.setBorderColorBottom(borderGray);
				TotalLetra.setBorderWidthBottom(2);
				TotalNumero.setBorder(Rectangle.BOTTOM);
				TotalNumero.setBorderColorBottom(borderGray);
				TotalNumero.setBorderWidthBottom(2);
				TotalNumero.setPaddingBottom(10f);
				TotalNumero.setPaddingTop(8f);
				
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				TotalLetra.setBorderColorBottom(borderTable);
				bloques.addCell(TotalLetra);
				bloques.addCell(cuerpoCoord);
				
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(subTotalLetra);
				bloques.addCell(subTotalNumero);
				
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(descuentoLetra);
				bloques.addCell(descuentoNumero);
				
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(ivaLetra);
				bloques.addCell(ivaNumero);
				
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				bloques.addCell(vacia);
				TotalLetra.setBorderColorBottom(borderGray);
				bloques.addCell(TotalLetra);
				bloques.addCell(TotalNumero);
			}	
			
			PdfPCell cuerpoBloque = new PdfPCell(bloques);
			cuerpoBloque.setBorder(0);
			tablaPrendas.addCell(cuerpoBloque);
		}
		
		
				//Primera tabla de numero, nombre y fecha

				// esta es la grande
				PdfPTable tablesplit = new PdfPTable(1);
				PdfPTable tableFirmas = new PdfPTable(1);
				PdfPTable tablesplit2 = new PdfPTable(1);
				PdfPCell observaciones = new PdfPCell();
				PdfPCell tituloTablaFirmas = new PdfPCell(new Phrase("OBSERVACIONES:", HelveticaBold));
		    	tituloTablaFirmas.setHorizontalAlignment(Element.ALIGN_LEFT);
				tituloTablaFirmas.setVerticalAlignment(Element.ALIGN_CENTER);
				tituloTablaFirmas.setBorder(0);
				tituloTablaFirmas.setPaddingTop(18f);
				tablesplit2.addCell(tituloTablaFirmas);
		    	try {
		    		observaciones = new PdfPCell(new Phrase(model.get("Observaciones").toString(), Helvetica));
		    	}
		    	catch(Exception e) {
		    		observaciones = new PdfPCell(new Phrase("", Helvetica));
		    	}
		    	
		    	observaciones.setBorder(0);
		    	observaciones.setPaddingTop(10f);
		    	observaciones.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
		    	tablesplit2.addCell(observaciones);
		    	tablesplit2.setWidthPercentage(100);
		    	PdfPTable tablaFooter1 = new PdfPTable(1);
		    	tituloTablaFirmas = new PdfPCell(new Phrase("CONDICIONES:", HelveticaBold));
		    	tituloTablaFirmas.setHorizontalAlignment(Element.ALIGN_LEFT);
				tituloTablaFirmas.setVerticalAlignment(Element.ALIGN_CENTER);
				tituloTablaFirmas.setBorder(0);
				tituloTablaFirmas.setPaddingTop(18f);
				tablaFooter1.addCell(tituloTablaFirmas);
		    	tablaFooter1.setWidthPercentage(100);
		    	PdfPCell leyenda2 = new PdfPCell(new Phrase("* Estos precios son más I.V.A.", letraCondiciones));
		    	PdfPCell leyenda3 = new PdfPCell(new Phrase("* El pago será de 45% días hábiles para un máximo de 50 personas y de 60 días hábiles para un número mayor; a partir del anticipo, toma de tallas, modelos y colores autorizados por uds.", letraCondiciones));
		    	PdfPCell leyenda4 = new PdfPCell(new Phrase("* Los uniformes son sobre talla y no sobre medida.", letraCondiciones));
		    	PdfPCell leyenda5 = new PdfPCell(new Phrase("* Los ajustes se entregarán en un máximo de 3 semanas a partir de su toma total.", letraCondiciones));
		    	PdfPCell leyenda6 = new PdfPCell(new Phrase("* Para el tiempo de entrega no se contarán la semana santa y las ultimas dos semanas de Diciembre.", Helvetica));
		    	PdfPCell leyenda7 = new PdfPCell(new Phrase("* Vigencia de la cotización: 15 días.", letraCondiciones));		    	
		    	
		    	leyenda2.setBorder(0);
		    	leyenda3.setBorder(0);
		    	leyenda4.setBorder(0);
		    	leyenda5.setBorder(0);
		    	leyenda6.setBorder(0);
		    	leyenda7.setBorder(0);
		    	leyenda2.setPadding(1.5f);
		    	leyenda2.setPaddingTop(20f);
		    	leyenda3.setPadding(1.5f);
		    	leyenda4.setPadding(1.5f);
		    	leyenda5.setPadding(1.5f);
		    	leyenda6.setPadding(1.5f);
		    	leyenda7.setPadding(1.5f);
		    	leyenda2.setHorizontalAlignment(Element.ALIGN_LEFT);
		    	leyenda3.setHorizontalAlignment(Element.ALIGN_LEFT);
		    	leyenda4.setHorizontalAlignment(Element.ALIGN_LEFT);
		    	leyenda5.setHorizontalAlignment(Element.ALIGN_LEFT);
		    	leyenda6.setHorizontalAlignment(Element.ALIGN_LEFT);
		    	leyenda7.setHorizontalAlignment(Element.ALIGN_LEFT);
		    
		    	tablaFooter1.addCell(leyenda2);
		    	tablaFooter1.addCell(leyenda3);
		    	tablaFooter1.addCell(leyenda4);
		    	tablaFooter1.addCell(leyenda5);
		    	tablaFooter1.addCell(leyenda6);
				tablaFooter1.addCell(leyenda7);
				
		    	//Segunda tabla de las firmas
		    	PdfPTable tablaFooter2 = new PdfPTable(3);
		    	tablaFooter2.setWidthPercentage(90);
		    	PdfPCell espacioBlanco1 = new PdfPCell(new Phrase(" "));
		    	PdfPCell leyenda8 = new PdfPCell(new Phrase("Atentamente: ", HelveticaBold));
		    	PdfPCell espacioBlanco2 = new PdfPCell(new Phrase(" "));
		    	PdfPCell espacioBlanco3 = new PdfPCell(new Phrase(" "));
		    	espacioBlanco1.setBorder(0);
		    	leyenda8.setBorder(0);
		    	espacioBlanco2.setBorder(0);
		    	espacioBlanco3.setBorder(0);
		    	leyenda8.setPadding(1.5f);
		    	espacioBlanco1.setHorizontalAlignment(Element.ALIGN_LEFT);
		    	leyenda8.setHorizontalAlignment(Element.ALIGN_CENTER);
		    	espacioBlanco2.setHorizontalAlignment(Element.ALIGN_RIGHT);
		    	espacioBlanco3.setHorizontalAlignment(Element.ALIGN_CENTER);
		    	tablaFooter2.addCell(espacioBlanco1);
		    	tablaFooter2.addCell(leyenda8);
		    	tablaFooter2.addCell(espacioBlanco2);
		    	//
		    	tablaFooter2.addCell(espacioBlanco1);
		    	tablaFooter2.addCell(espacioBlanco3);
		    	tablaFooter2.addCell(espacioBlanco2);
		    	//Firmas ya pro
		    	Image firmaVentas = Image.getInstance("src/main/resources/static/dist/img/firmaVentas.png");
		    	firmaVentas.scalePercent(40f);
		    	
		    	PdfPCell firmaImgVentas = new PdfPCell(firmaVentas);
		    	firmaImgVentas.setBorder(0);
		    	firmaImgVentas.setHorizontalAlignment(Element.ALIGN_CENTER);
		    	tablaFooter2.addCell(firmaImgVentas);
		    	
		    	PdfPCell firmaActual = new PdfPCell(new Phrase(" "));
		    	firmaActual.setBorder(0);
		    	Image firma = Image.getInstance("src/main/resources/static/dist/img/firma.png");
		    	
		    	firma.scalePercent(10f);
		    	
		    	tablaFooter2.addCell(firmaActual);
		    	PdfPCell firmaImg = new PdfPCell(firma);
		    	firmaImg.setBorder(0);
		    	firmaImg.setPaddingTop(1.5f);
		    	firmaImg.setHorizontalAlignment(Element.ALIGN_CENTER);
		    	tablaFooter2.addCell(firmaImg);
		    	//Espacios para firmar
		    	PdfPCell espacioFirma1 = new PdfPCell(new Phrase("________________________"));
		    	PdfPCell espacioFirma2 = new PdfPCell(new Phrase("________________________"));
		    	espacioFirma1.setBorder(0);
		    	espacioFirma2.setBorder(0);
		    	espacioFirma1.setHorizontalAlignment(Element.ALIGN_CENTER);
		    	espacioFirma2.setHorizontalAlignment(Element.ALIGN_CENTER);
		    	tablaFooter2.addCell(espacioFirma1);
		    	tablaFooter2.addCell(espacioBlanco3);
		    	tablaFooter2.addCell(espacioFirma2);
		    	//Nombres de las firmas
		    	PdfPCell nombreFirma1 = new PdfPCell(new Phrase("Diana Rodriguez", Helvetica));
		    	PdfPCell nombreFirma2 = new PdfPCell(new Phrase("Jorge Armando Cottone Morales", Helvetica));
		    	nombreFirma1.setBorder(0);
				nombreFirma1.setHorizontalAlignment(Element.ALIGN_CENTER);
		    	nombreFirma2.setBorder(0);
				nombreFirma2.setHorizontalAlignment(Element.ALIGN_CENTER);
		    	tablaFooter2.addCell(nombreFirma1);
		    	tablaFooter2.addCell(espacioBlanco3);
		    	tablaFooter2.addCell(nombreFirma2);
		    	//Puestos firmas
		    	PdfPCell puestosFirma1 = new PdfPCell(new Phrase("Ejecutivo de Ventas", Helvetica));
		    	PdfPCell puestosFirma2 = new PdfPCell(new Phrase("Gerente Comercial", Helvetica));
		    	puestosFirma1.setBorder(0);
		    	puestosFirma1.setHorizontalAlignment(Element.ALIGN_CENTER);
		    	puestosFirma2.setBorder(0);
		    	puestosFirma2.setHorizontalAlignment(Element.ALIGN_CENTER);
		    	tablaFooter2.addCell(puestosFirma1);
		    	tablaFooter2.addCell(espacioBlanco3);
		    	tablaFooter2.addCell(puestosFirma2);
				tablaFooter2.setWidths(new float[] { 6f, 3f, 6f });
				

				PdfPCell cellsplit = new PdfPCell(tablaFooter1);
				cellsplit.setBorder(PdfPCell.NO_BORDER);
				tablesplit.addCell(cellsplit);
				
				cellsplit = new PdfPCell(tablaFooter2);
				cellsplit.setBorder(PdfPCell.NO_BORDER);
				tablesplit.addCell(cellsplit);
				tablesplit.setWidthPercentage(100);
				
				cellsplit = new PdfPCell(tablesplit);
				cellsplit.setBorder(PdfPCell.NO_BORDER);
				PdfPCell cellsplit2 = new PdfPCell(tablesplit2);
				cellsplit2.setBorder(PdfPCell.NO_BORDER);
				tableFirmas.addCell(cellsplit2);
				tableFirmas.addCell(cellsplit);
				tableFirmas.setWidthPercentage(100);
		    	
		//Aqui se hace el merge del CV si es que existe
		if(cv) {
			HeaderFooterCotizacionesPdfView event = new HeaderFooterCotizacionesPdfView();
			writer.setPageEvent(event);
			document.open();
			document.addTitle("Cotizacion" + (String) model.get("id") + "_" + formatter.print(jodaTime));
			document.add(tablaNotaria);
			document.add(espacio);
			document.add(tableInfo);
			document.add(espacio);
			document.add(tablaHeader1);
			document.add(tablaHeader2);
			document.add(espacio);
			document.add(tablaDatosCotizacion);
			document.add(tablaHeader3);
			document.add(espacio);
			document.add(tablaPrendas);
			document.add(tableFirmas);
			document.add(espacio);

			document.newPage();
			PdfPTable tableCV = new PdfPTable(1);
			PdfPCell tituloCV = celd;
			tituloCV.setHorizontalAlignment(Element.ALIGN_LEFT);
			tituloCV.setVerticalAlignment(Element.ALIGN_CENTER);
			tituloCV.setBorder(0);
			tituloCV.setPaddingTop(28f);
			tableCV.addCell(tituloCV);
			
			tituloCV = new PdfPCell(new Phrase("At'n: " + cliente.getNombreContacto(), TitulosOscuros));
			tituloCV.setHorizontalAlignment(Element.ALIGN_LEFT);
			tituloCV.setVerticalAlignment(Element.ALIGN_CENTER);
			tituloCV.setBorder(0);
			tituloCV.setPaddingTop(18f);
			tableCV.addCell(tituloCV);
			
			tituloCV = new PdfPCell(new Phrase("Presente:", TitulosOscuros));
			tituloCV.setHorizontalAlignment(Element.ALIGN_LEFT);
			tituloCV.setVerticalAlignment(Element.ALIGN_CENTER);
			tituloCV.setBorder(0);
			tituloCV.setPaddingTop(18f);
			tableCV.addCell(tituloCV);
			
			PdfPCell EspacioCV = new PdfPCell();
			EspacioCV.setBorder(0);
			EspacioCV.setPadding(15f);
			tableCV.addCell(EspacioCV);
			Font negrita = new Font(BaseFont.createFont( BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.EMBEDDED), 11);
			Chunk ALTIMA = new Chunk("ALTIMA", negrita);
			Chunk Altima = new Chunk("ALTIMA.", negrita);
			
			Paragraph textoCompleto1 = new Paragraph("Somos una empresa joven y dinámica especializada en el diseño y confección de uniformes corporativos e institucionales.\r\n" + "\r\n" +
					   "En ", Titulos);
			
			Paragraph textoCompleto2 = new Paragraph(" entendemos plenamente las necesidades de las empresas y las expectativas de los usuarios finales " + 
					   "de nuestros uniformes. Por eso, el principal objetivo que tenemos es ofrecer soluciones versátiles para cada " + 
					   "tipo de necesidades y satisfacer con creces las expectativas de buen gusto, actualidad y distinción de quienes " + 
					   "visten uniformes ", Titulos);
			textoCompleto2.add(Altima);
			Paragraph textoCompleto3 = new Paragraph("\r\n" + 
					   "En consecuencia, aplicamos rigurosos controles de calidad en la selección de las telas que utilizamos y en la " + 
					   "ejecución de los distintos procesos de corte, confección y acabados; asimismo proyectamos las últimas " + 
					   "tendencias de la moda en el diseno de cada una de las prendas que elaboramos. Es así que podemos ofrecer " + 
					   "a los usuarios ejecutivos, uniformes que puede lucir con satisfacción y agrado dentro y fuera de su ámbito de trabajo.\r\n" + "\r\n" + 
					   "Coincidimos con usted en que el capital más valioso de su empresa es su personal y en que la buena imagen " + 
					   "del mismo es una inversión que contribuye significativamente a crear y mantener entre sus clientes y " + 
					   "proveedores una percepción positiva de su negocio.\r\n" + "\r\n" + 
					   "Por lo mismo, lo invitamos a que conozca en detalle las ventajas competitivas de nuestros productos y servicio " + 
					   "y nos brinde pronto la oportunidad de demostrarles porque ", Titulos);
			
			Paragraph textoCompleto4 = new Paragraph(" será siempre su mejor inversión en " + 
					   "uniformes corporativos e institucionales.\r\n" + "\r\n" + 
					   "Llámenos y con gusto uno de nuestros ejecutivos comerciales le visitara, brindándoles el servicio y atención " + 
					   "especializada que su empresa merece", Titulos);
			textoCompleto1.add(ALTIMA);
			textoCompleto1.add(textoCompleto2);
			
			textoCompleto2.add(textoCompleto3);
			textoCompleto3.add(ALTIMA);
			textoCompleto3.add(textoCompleto4);
			
			Paragraph textoCompleto = new Paragraph();
			textoCompleto = textoCompleto1;
			textoCompleto.add(textoCompleto3);
			
			
			PdfPCell ContenidoCV = new PdfPCell(textoCompleto);
			ContenidoCV.setBorder(0);
			ContenidoCV.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			ContenidoCV.setPaddingTop(10f);
			ContenidoCV.setPaddingBottom(10f);
			tableCV.addCell(ContenidoCV);
			
			EspacioCV = new PdfPCell();
			EspacioCV.setBorder(0);
			EspacioCV.setPadding(30f);
			tableCV.addCell(EspacioCV);
			
			PdfPCell firmaGerente = new PdfPCell(new Phrase(" "));
			firmaGerente.setBorder(0);
			firmaGerente.setPadding(3f);
	    	Image imgfirmaGerente = Image.getInstance("src/main/resources/static/dist/img/firma.png");
	    	
	    	imgfirmaGerente.scalePercent(10f);
	    	
	    	tableCV.addCell(firmaGerente);
	    	PdfPCell firmaImgGerente = new PdfPCell(imgfirmaGerente);
	    	firmaImgGerente.setBorder(0);
	    	firmaImgGerente.setPadding(3f);
	    	firmaImgGerente.setHorizontalAlignment(Element.ALIGN_CENTER);
	    	tableCV.addCell(firmaImgGerente);
	    	//Espacios para firmar
	    	PdfPCell espacioFirmaGerente = new PdfPCell(new Phrase("________________________"));
	    	espacioFirmaGerente.setBorder(0);
	    	espacioFirmaGerente.setBorder(0);
	    	espacioFirmaGerente.setPadding(3f);
	    	espacioFirmaGerente.setHorizontalAlignment(Element.ALIGN_CENTER);
	    	tableCV.addCell(espacioFirmaGerente);
			
	    	PdfPCell nombreFirmaGerente = new PdfPCell(new Phrase("Adán Gomez", Helvetica));
	    	PdfPCell puestoFirmaGerente = new PdfPCell(new Phrase("Director General", Helvetica));
	    	nombreFirmaGerente.setBorder(0);
	    	nombreFirmaGerente.setHorizontalAlignment(Element.ALIGN_CENTER);
	    	nombreFirmaGerente.setPaddingBottom(0.1f);
	    	puestoFirmaGerente.setBorder(0);
	    	puestoFirmaGerente.setHorizontalAlignment(Element.ALIGN_CENTER);
	    	puestoFirmaGerente.setPaddingBottom(0.1f);
	    	tableCV.addCell(nombreFirmaGerente);
	    	tableCV.addCell(puestoFirmaGerente);
	    	
	    	
			document.add(tableCV);
			
			document.newPage();
			PdfReader reader = new PdfReader(FILE1);
			PdfImportedPage page = writer.getImportedPage(reader, 3); 
			PdfContentByte cb = writer.getDirectContent();
			cb.addTemplate(page, 0, 0);
			document.newPage();
			reader = new PdfReader(FILE1);
			page = writer.getImportedPage(reader, 2); 
			cb = writer.getDirectContent();
			cb.addTemplate(page, 0, 0);
			document.close();
		}
		if(!cv) {
			HeaderFooterCotizacionesPdfView event = new HeaderFooterCotizacionesPdfView();
			writer.setPageEvent(event);
			document.open();
			document.addTitle("Cotizacion" + (String) model.get("id") + "_" + formatter.print(jodaTime));
			document.add(tablaNotaria);
			document.add(espacio);
			document.add(tableInfo);
			document.add(espacio);
			document.add(tablaHeader1);
			document.add(tablaHeader2);
			document.add(espacio);
			document.add(tablaDatosCotizacion);
			document.add(tablaHeader3);
			document.add(espacio);
			document.add(tablaPrendas);
			document.add(tableFirmas);
			document.add(espacio);
		}
		if(!correo.equalsIgnoreCase("nulo")) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			document = new Document(PageSize.A4);
			document.setMargins(40f, 40f, 10f, 100f);
			writer =  PdfWriter.getInstance(document, baos);
			HeaderFooterCotizacionesPdfView event = new HeaderFooterCotizacionesPdfView();
			writer.setPageEvent(event);
			document.open();
			document.add(new Chunk(""));
			document.addTitle("Cotizacion" + (String) model.get("id") + "_" + formatter.print(jodaTime));
			document.add(tablaNotaria);
			document.add(espacio);
			document.add(tableInfo);
			document.add(espacio);
			document.add(tablaHeader1);
			document.add(tablaHeader2);
			document.add(espacio);
			document.add(tablaDatosCotizacion);
			document.add(tablaHeader3);
			document.add(espacio);
			document.add(tablaPrendas);
			document.add(tableFirmas);
			document.add(espacio);

			document.newPage();
			PdfPTable tableCV = new PdfPTable(1);
			PdfPCell tituloCV = celd;
			tituloCV.setHorizontalAlignment(Element.ALIGN_LEFT);
			tituloCV.setVerticalAlignment(Element.ALIGN_CENTER);
			tituloCV.setBorder(0);
			tituloCV.setPaddingTop(28f);
			tableCV.addCell(tituloCV);
			
			tituloCV = new PdfPCell(new Phrase("At'n: " + cliente.getNombreContacto(), TitulosOscuros));
			tituloCV.setHorizontalAlignment(Element.ALIGN_LEFT);
			tituloCV.setVerticalAlignment(Element.ALIGN_CENTER);
			tituloCV.setBorder(0);
			tituloCV.setPaddingTop(18f);
			tableCV.addCell(tituloCV);
			
			tituloCV = new PdfPCell(new Phrase("Presente:", TitulosOscuros));
			tituloCV.setHorizontalAlignment(Element.ALIGN_LEFT);
			tituloCV.setVerticalAlignment(Element.ALIGN_CENTER);
			tituloCV.setBorder(0);
			tituloCV.setPaddingTop(18f);
			tableCV.addCell(tituloCV);
			
			PdfPCell EspacioCV = new PdfPCell();
			EspacioCV.setBorder(0);
			EspacioCV.setPadding(15f);
			tableCV.addCell(EspacioCV);
			Font negrita = new Font(BaseFont.createFont( BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.EMBEDDED), 11);
			Chunk ALTIMA = new Chunk("ALTIMA", negrita);
			Chunk Altima = new Chunk("ALTIMA.", negrita);
			
			Paragraph textoCompleto1 = new Paragraph("Somos una empresa joven y dinámica especializada en el diseño y confección de uniformes corporativos e institucionales.\r\n" + "\r\n" +
					   "En ", Titulos);
			
			Paragraph textoCompleto2 = new Paragraph(" entendemos plenamente las necesidades de las empresas y las expectativas de los usuarios finales " + 
					   "de nuestros uniformes. Por eso, el principal objetivo que tenemos es ofrecer soluciones versátiles para cada " + 
					   "tipo de necesidades y satisfacer con creces las expectativas de buen gusto, actualidad y distinción de quienes " + 
					   "visten uniformes ", Titulos);
			textoCompleto2.add(Altima);
			Paragraph textoCompleto3 = new Paragraph("\r\n" + 
					   "En consecuencia, aplicamos rigurosos controles de calidad en la selección de las telas que utilizamos y en la " + 
					   "ejecución de los distintos procesos de corte, confección y acabados; asimismo proyectamos las últimas " + 
					   "tendencias de la moda en el diseno de cada una de las prendas que elaboramos. Es así que podemos ofrecer " + 
					   "a los usuarios ejecutivos, uniformes que puede lucir con satisfacción y agrado dentro y fuera de su ámbito de trabajo.\r\n" + "\r\n" + 
					   "Coincidimos con usted en que el capital más valioso de su empresa es su personal y en que la buena imagen " + 
					   "del mismo es una inversión que contribuye significativamente a crear y mantener entre sus clientes y " + 
					   "proveedores una percepción positiva de su negocio.\r\n" + "\r\n" + 
					   "Por lo mismo, lo invitamos a que conozca en detalle las ventajas competitivas de nuestros productos y servicio " + 
					   "y nos brinde pronto la oportunidad de demostrarles porque ", Titulos);
			
			Paragraph textoCompleto4 = new Paragraph(" será siempre su mejor inversión en " + 
					   "uniformes corporativos e institucionales.\r\n" + "\r\n" + 
					   "Llámenos y con gusto uno de nuestros ejecutivos comerciales le visitara, brindándoles el servicio y atención " + 
					   "especializada que su empresa merece", Titulos);
			textoCompleto1.add(ALTIMA);
			textoCompleto1.add(textoCompleto2);
			
			textoCompleto2.add(textoCompleto3);
			textoCompleto3.add(ALTIMA);
			textoCompleto3.add(textoCompleto4);
			
			Paragraph textoCompleto = new Paragraph();
			textoCompleto = textoCompleto1;
			textoCompleto.add(textoCompleto3);
			
			
			PdfPCell ContenidoCV = new PdfPCell(textoCompleto);
			ContenidoCV.setBorder(0);
			ContenidoCV.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			ContenidoCV.setPaddingTop(10f);
			ContenidoCV.setPaddingBottom(10f);
			tableCV.addCell(ContenidoCV);
			
			EspacioCV = new PdfPCell();
			EspacioCV.setBorder(0);
			EspacioCV.setPadding(30f);
			tableCV.addCell(EspacioCV);
			
			PdfPCell firmaGerente = new PdfPCell(new Phrase(" "));
			firmaGerente.setBorder(0);
			firmaGerente.setPadding(3f);
	    	Image imgfirmaGerente = Image.getInstance("src/main/resources/static/dist/img/firma.png");
	    	
	    	imgfirmaGerente.scalePercent(10f);
	    	
	    	tableCV.addCell(firmaGerente);
	    	PdfPCell firmaImgGerente = new PdfPCell(imgfirmaGerente);
	    	firmaImgGerente.setBorder(0);
	    	firmaImgGerente.setPadding(3f);
	    	firmaImgGerente.setHorizontalAlignment(Element.ALIGN_CENTER);
	    	tableCV.addCell(firmaImgGerente);
	    	//Espacios para firmar
	    	PdfPCell espacioFirmaGerente = new PdfPCell(new Phrase("________________________"));
	    	espacioFirmaGerente.setBorder(0);
	    	espacioFirmaGerente.setBorder(0);
	    	espacioFirmaGerente.setPadding(3f);
	    	espacioFirmaGerente.setHorizontalAlignment(Element.ALIGN_CENTER);
	    	tableCV.addCell(espacioFirmaGerente);
			
	    	PdfPCell nombreFirmaGerente = new PdfPCell(new Phrase("Adán Gomez", Helvetica));
	    	PdfPCell puestoFirmaGerente = new PdfPCell(new Phrase("Director General", Helvetica));
	    	nombreFirmaGerente.setBorder(0);
	    	nombreFirmaGerente.setHorizontalAlignment(Element.ALIGN_CENTER);
	    	nombreFirmaGerente.setPaddingBottom(0.1f);
	    	puestoFirmaGerente.setBorder(0);
	    	puestoFirmaGerente.setHorizontalAlignment(Element.ALIGN_CENTER);
	    	puestoFirmaGerente.setPaddingBottom(0.1f);
	    	tableCV.addCell(nombreFirmaGerente);
	    	tableCV.addCell(puestoFirmaGerente);
	    	
	    	
			document.add(tableCV);
			
			document.newPage();
			PdfReader reader = new PdfReader(FILE1);
			PdfImportedPage page = writer.getImportedPage(reader, 3); 
			PdfContentByte cb = writer.getDirectContent();
			cb.addTemplate(page, 0, 0);
			document.newPage();
			reader = new PdfReader(FILE1);
			page = writer.getImportedPage(reader, 2); 
			cb = writer.getDirectContent();
			cb.addTemplate(page, 0, 0);
			document.close();
			enviarCorreoService.enviarCorreoArchivoAdjuntoConMime("dtu_test@uniformes-altima.com.mx", correo, "Envio de Cotización", "A continuación se anexa un informe de la cotización solicitada.", baos, "Cotizacion" + (String) model.get("id") + "_" + formatter.print(jodaTime) + ".pdf");
		}
	}
}
