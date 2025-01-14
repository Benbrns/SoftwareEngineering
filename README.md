# Car Rental Service

Dit project is een **Car Rental Service**, ontwikkeld als onderdeel van het vak **Software Engineering**. Het omvat zowel een krachtige backend als een interactieve frontend, waarbij verschillende software engineering-concepten en tools zijn toegepast.

## Overzicht

De Car Rental Service biedt gebruikers de mogelijkheid om voertuigen te reserveren, beheren en huren via een gebruiksvriendelijke interface. Zowel klanten als beheerders hebben toegang tot specifieke functionaliteiten afhankelijk van hun rechten. Dit project is ontwikkeld door een team van **6 personen** en volgde het **scrum-principe** om een iteratieve en efficiënte ontwikkeling te garanderen.

---

## Functionaliteiten

### Gebruiker

- **Registreren en inloggen** met beveiliging via JWT.
- **Zoeken naar voertuigen** op basis van beschikbaarheid, type en locatie.
- **Reserveren** van voertuigen.
- **Beheren van reserveringen** (bijvoorbeeld annuleren).

### Beheerder

- **Toevoegen, verwijderen en wijzigen** van voertuigen in de vloot.
- **Overzicht** van alle reserveringen.
- **Beheren van gebruikers**.

---

## Technologieën en Tools

### Backend

- **Spring Boot**:
  - REST API met gedocumenteerde endpoints.
  - **Beveiliging** van endpoints met JWT authenticatie en autorisatie.
- **Flyway**:
  - Automatische versiebeheer van database migraties.
- **PostgreSQL**:
  - Relationele database voor efficiënte opslag en beheer van gegevens.

### Frontend

- **React**:
  - Dynamische en responsive gebruikersinterface.

### Hosting en Automatisatie

- **Microsoft Azure**:
  - Hosting van de applicatie en database.
- **CI/CD**:
  - Geautomatiseerde build- en deployment-pijplijnen via Azure DevOps/GitHub Actions.

---

## Scrum en Samenwerking

- **Scrum-principe**:
  - Wekelijkse **stand-ups** en **retrospectives**.
  - **Product backlog** en **sprint backlog**.
- Rollen in het team:
  - Product Owner, Scrum Master, en Development Team.
- Actieve samenwerking via **Git** en **GitHub** voor versiebeheer.

---

## Documentatie

- **Swagger**:
  - Gedetailleerde documentatie van API-endpoints.
  - Eenvoudig te gebruiken interface om endpoints te testen.

---

## Installatie en Gebruik

### Vereisten

- **Node.js** en **npm** (voor frontend).
- **Java 17+** en **Maven** (voor backend).
- Een PostgreSQL-database.

### Installatiestappen

1. **Backend**:

   - Ga naar de map `/software-engineering-backend-groep02-main`.
   - Voer `mvn clean install` uit.
   - Start de applicatie met `mvn spring-boot:run`.

2. **Frontend**:

   - Ga naar de map `/software-engineering-frontend-groep02-main`.
   - Maak een `.env`-bestand aan in de rootmap en voeg de volgende configuratie toe:

     ```plaintext
     NEXT_PUBLIC_API_URL=http://localhost:8080
     NEXTAUTH_URL=http://localhost:3000
     NEXTAUTH_SECRET=0sNpglHDrdrPpdIVqsJmMZp4BWpgP+y02sT+KynRpw7SpuxYgqNsym5QumXECgAhEMrGF/gNlsGGKtEKXuEVLA==
     ```

     > **Let op:** Dit bestand bevat gevoelige informatie en mag normaal niet worden gedeeld. Voor demonstratiedoeleinden wordt deze configuratie hier opgenomen.

   - Installeer dependencies met `npm install`.
   - Start de applicatie met `npm run dev`.

3. **Database**:

   - Configureer de database in `application.properties` in de backend.
   - Flyway voert de migraties automatisch uit bij het starten.

4. **CI/CD**:
   - CI/CD-pijplijnen voeren automatisch tests, builds en deploys uit bij iedere push naar de repository.

---

## Leerpunten

Tijdens dit project hebben we ervaring opgedaan met:

- **Full-stack ontwikkeling**: integratie van frontend en backend.
- **Scrum**: werken in een agile teamomgeving.
- **Beveiliging**: gebruik van JWT voor veilige toegang.
- **Cloud-hosting**: deployen op Azure.
- **Databasebeheer**: gebruik van Flyway voor migraties.
- **CI/CD**: geautomatiseerde builds en deploys met Azure DevOps/GitHub Actions.

---
