# COM2002 Database Group Project
## MySQL Login Details
Constants defined in src/com2002/team021/config/SQL.java

## Team Jobs

### MySQL
- Rob
- Moritz

### GUI
- Afam
- Josh

## Useful MySQL Snippets
- Getting patients' address
```
SELECT * FROM addresses
	WHERE (houseNumber, postcode)
	IN (SELECT houseNumber, postcode
		FROM patients
		WHERE id = ?)
	LIMIT 1;
```

- Getting all appointments and all asscociated treatments 
```
SELECT * FROM appointments
	NATURAL JOIN (sessions JOIN treatments
		ON (treatments.name = sessions.treatmentName))
```

