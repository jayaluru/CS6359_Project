<%@ page import="java.util.*" %>    
<%@page import="domain.user.Cart"%>
<%@page import="db.services.CartPersistenceService"%>
<%@page import="db.services.impl.CartPersistenceServiceImpl"%>
<%@page import="domain.product.Product"%>
<%@page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>CartPage</title>
</head>
<style>
.content {
    max-width: 1000px;
    margin: auto;
    background: white;
    padding: 50px;
    line-height: 0.3;
}
</style>
<body>
	<% 
	HttpSession sess = request.getSession(true);
	Integer userId = (Integer) sess.getAttribute("userId");
	if (userId == null){
		response.sendRedirect("login.jsp");
		return;
	}
	Integer invnId = (Integer) sess.getAttribute("invnId");
	Integer cartId = (Integer) sess.getAttribute("cartId");
	String name = (String) sess.getAttribute("name");
	%>
	<div class="menu" align = "Center">
		<a href="home.jsp">Home</a>
		<a href="category.jsp">Category</a>
		<a href="cart.jsp">Cart</a>
		<a href="inventory.jsp">Inventory</a>
		<a href="transactions.jsp">Transactions</a>
		<a href="about.jsp">About</a>
		<a href="faq.jsp" >FAQs</a>
		<a href="logout.jsp" >Logout</a>
 	</div>
 	<hr>
	<h4>Cart:</h4>
	 <%
	CartPersistenceService cartService = new CartPersistenceServiceImpl();
	Cart cart = cartService.retrieve(userId);
	
	List<Product> prods = cart.getProducts();
	double totalPrice = 0.0;
	if (prods.size() > 0){
	
	%>
	<table border="1" style="margin-top: 20px; margin-right: 20px; margin-left: 29px; border-top-width: 2px;">
		<tr>
			<th>Name</th>
			<th>Description</th>
			<th>Price</th>
			<th>Action</th>   
		</tr>
	     
		<%for(Product prod : prods) {%>
			<tr>
				<td><%= prod.getName() %></td>
				<td><%= prod.getDescription() %></td>
				<td><%= prod.getPrice() %></td>
				<td>
					<form name="detailsform" action="DetailsController" method="post">
						<input type="hidden" name="prodId" value="<%= prod.getProdId().toString() %>">
						<input type="hidden" name="catId" value="<%= prod.getCategory().getCatId().toString() %>">
						<input class="demo" type="submit" name="ViewDetails" value = "View Details" style="left: 460px;">
						<input class="demo" type="submit" name="Remove" value="Remove Item" style="left: 460px;">
					</form>
				</td>
			</tr>
			<% totalPrice += prod.getPrice(); %>
		<%}%>
	</table>
	<h5>Total Price: <%= totalPrice %></h5>
	<form name="checkoutForm" action="CheckoutController" method="post">
		<input class="demo" type="submit" name="Checkout" value = "Check Out" style="left: 460px;">
	</form>
	<% } else {%>
		<p> Your Cart is empty. </p>
	<%}%>
</body>
</html>