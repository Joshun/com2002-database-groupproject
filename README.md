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

- Best match patient search (given surname OR forename)
```
SELECT * FROM patients
WHERE surname LIKE '%rob%'
UNION
SELECT * FROM patients
WHERE forename LIKE '%rob%'
ORDER BY
	CASE WHEN surname = 'rob' THEN 0
	WHEN surname LIKE 'rob%' THEN 1
	WHEN surname LIKE '%rob%' THEN 2
	WHEN forename = 'rob' THEN 3
	WHEN forename LIKE 'rob%' THEN 4
	WHEN forename LIKE '%rob%' THEN 5
	ELSE 6
END, id DESC
```

- Best match patient search (given both names)
```
SELECT * FROM patients
WHERE forename LIKE '%rob%' AND surname LIKE '%ede%'
ORDER BY
	CASE WHEN forename = 'rob' AND surname = 'ede' THEN 0
	WHEN forename LIKE 'rob%' AND surname = 'ede' THEN 1
	WHEN forename LIKE 'rob%' AND surname LIKE 'ede%' THEN 2
	WHEN forename LIKE '%rob%' AND surname LIKE '%ede%' THEN 3
	ELSE 4
END, id DESC;
```
