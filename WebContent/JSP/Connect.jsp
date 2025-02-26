<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Tableau de bord</title>
<link rel="stylesheet" href="/_00_ASBank2023/style/style.css" />
</head>
<body>
	<div class="btnLogout">
		<s:form name="myForm" action="logout" method="POST">
			<s:submit name="Retour" value="Logout" />
		</s:form>
	</div>
	<h1>Tableau de bord</h1>
	<p>
		Bienvenue <strong><s:property value="connectedUser.prenom" /> <s:property
				value="connectedUser.nom" /></strong> !
	</p>
	<p>Voici l'état de vos comptes :</p>
	<table>
		<tr>
			<td><strong>Numéro de compte</strong></td>
			<td><strong>Type de compte</strong></td>
			<td><strong>Solde actuel</strong></td>
		</tr>
		<s:iterator value="accounts">
			<tr>
				<td>
					<s:form action="urlDetail" method="POST">
						<s:hidden name="compte" value="%{key}" />
						<s:submit value="%{key}" />
					</s:form>
				</td>
				<s:if test="%{value.className == \"CompteAvecDecouvert\"}">
					<td>Découvert possible</td>
				</s:if>
				<s:else>
					<td>Simple</td>
				</s:else>
				<s:if test="%{value.solde >= 0}">
					<td><s:property value="value.solde" /></td>
				</s:if>
				<s:else>
					<td class="soldeNegatif"><s:property value="value.solde" /></td>
				</s:else>
			</tr>
		</s:iterator>
	</table>
</body>
<jsp:include page="/JSP/Footer.jsp" />
</html>