package domain.product;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import db.services.CategoryPersistenceService;
import db.services.CraftPersistenceService;
import db.services.impl.CategoryPersistenceServiceImpl;
import db.services.impl.CraftPersistenceServiceImpl;

/**
 * Servlet implementation class CraftController
 */
@MultipartConfig
@WebServlet("/CraftController")
public class CraftController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private CraftPersistenceService craftService = new CraftPersistenceServiceImpl();
	private CategoryPersistenceService catService = new CategoryPersistenceServiceImpl();
	private static Integer catId = 3; 
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession sess = request.getSession(true);
		Integer invnId = (Integer) sess.getAttribute("invnId");
		
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		Double price = Double.parseDouble(request.getParameter("price"));
		String usage = request.getParameter("usage");
		Double length = Double.parseDouble(request.getParameter("length"));
		Double width = Double.parseDouble(request.getParameter("width"));
		Double height = Double.parseDouble(request.getParameter("height"));
		
		// obtains the upload file part in this multipart request
        Part filePart = request.getPart("file");
        // obtains input stream of the upload file
        InputStream inputStream = filePart.getInputStream();
        BufferedImage image = ImageIO.read(inputStream);

		Craft craft = new Craft();
		craft.setName(name);
		craft.setDescription(description);
		craft.setPrice(price);
		craft.setSold(false);
		craft.setUsage(usage);
		craft.setLength(length);
		craft.setWidth(width);
		craft.setHeight(height);
		craft.setImage(image);

		try {
			Category cat = catService.retrieve(catId);
			craft.setCategory(cat);			
			craftService.create(craft, invnId);
		} catch (Exception ex) {
			System.out.println(ex);
			// TODO return failure message
		}
		
		RequestDispatcher rs = request.getRequestDispatcher("inventory.jsp");
		rs.forward(request, response);
	}
}
