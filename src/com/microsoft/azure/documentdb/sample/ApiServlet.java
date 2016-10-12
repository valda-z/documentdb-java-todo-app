package com.microsoft.azure.documentdb.sample;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microsoft.azure.documentdb.sample.model.CertItem;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.microsoft.azure.documentdb.sample.controller.TodoItemController;

/**
 * API Frontend Servlet
 */
@WebServlet("/api")
public class ApiServlet extends HttpServlet {
	// API Keys
	public static final String API_METHOD = "method";

	// API Methods
	public static final String CREATE_TODO_ITEM = "createTodoItem";
	public static final String GET_CERTINFO = "getCertInfo";
	public static final String GET_TODO_ITEMS = "getTodoItems";
	public static final String UPDATE_TODO_ITEM = "updateTodoItem";

	// API Parameters
	public static final String TODO_ITEM_ID = "todoItemId";
	public static final String TODO_ITEM_NAME = "todoItemName";
	public static final String TODO_ITEM_CATEGORY = "todoItemCategory";
	public static final String TODO_ITEM_COMPLETE = "todoItemComplete";

	public static final String MESSAGE_ERROR_INVALID_METHOD = "{'error': 'Invalid method'}";

	private static final long serialVersionUID = 1L;
	private static final Gson gson = new Gson();

	protected static String getThumbPrint(X509Certificate cert)
			throws NoSuchAlgorithmException, CertificateEncodingException {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		byte[] der = cert.getEncoded();
		md.update(der);
		byte[] digest = md.digest();
		return hexify(digest);

	}

	protected static String hexify (byte bytes[]) {

		char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7',
				'8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

		StringBuffer buf = new StringBuffer(bytes.length * 2);

		for (int i = 0; i < bytes.length; ++i) {
			buf.append(hexDigits[(bytes[i] & 0xf0) >> 4]);
			buf.append(hexDigits[bytes[i] & 0x0f]);
		}

		return buf.toString();
	}


	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String apiResponse = MESSAGE_ERROR_INVALID_METHOD;

		TodoItemController todoItemController = TodoItemController
				.getInstance();

		String id = request.getParameter(TODO_ITEM_ID);
		String name = request.getParameter(TODO_ITEM_NAME);
		String category = request.getParameter(TODO_ITEM_CATEGORY);
		boolean isComplete = StringUtils.equalsIgnoreCase("true",
				request.getParameter(TODO_ITEM_COMPLETE)) ? true : false;

		switch (request.getParameter(API_METHOD)) {
		case CREATE_TODO_ITEM:
			apiResponse = gson.toJson(todoItemController.createTodoItem(name,
					category, isComplete));
			break;
		case GET_TODO_ITEMS:
			apiResponse = gson.toJson(todoItemController.getTodoItems());
			break;
		case UPDATE_TODO_ITEM:
			apiResponse = gson.toJson(todoItemController.updateTodoItem(id,
					isComplete));
			break;
		case GET_CERTINFO:
			CertItem itm = new CertItem();
			String ret = "N/A";
			{
				if(request.getHeader("X-ARR-ClientCert") != null){
					ret = request.getHeader("X-ARR-ClientCert");
					try {
						X509Certificate publicKey = (X509Certificate)CertificateFactory
								.getInstance("X.509")
								.generateCertificate(new ByteArrayInputStream(Base64.decodeBase64(ret)));
						ret = "Subject: " + publicKey.getSubjectDN().getName() +
								" | Issuer: " + publicKey.getIssuerDN().getName() +
								" | Thumbprint: " + getThumbPrint(publicKey);
					} catch (CertificateException e) {
						e.printStackTrace();
					} catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    //
				}
			}
			itm.setRet(ret);
			apiResponse = gson.toJson(itm);
			break;
		default:
			break;
		}

		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(apiResponse);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
