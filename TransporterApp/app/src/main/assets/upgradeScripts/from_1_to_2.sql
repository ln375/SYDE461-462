UPDATE jug
SET transporter_id = (CASE WHEN id='2' THEN '2'
		WHEN id='3' THEN '3'
		WHEN id='4' THEN '4'
		WHEN id='5' THEN '5'
		WHEN id='6' THEN '6'
		WHEN id='7' THEN '7'
		WHEN id='8' THEN '8'
		WHEN id='9' THEN '3'
		WHEN id='10' THEN '6'
		WHEN id='11' THEN '5'
		WHEN id='12' THEN '7'
		WHEN id='13' THEN '2'
		WHEN id='14' THEN '4'
		WHEN id='15' THEN '1'
		WHEN id='16' THEN '8' end);