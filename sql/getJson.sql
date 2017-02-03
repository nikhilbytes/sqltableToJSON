-- --------------------------------------------------------------------------------
-- Auther--Nikhil Tripathi
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `chartRelation`(V int)
BEGIN

set @ff = V;
set @ConcatedString = 0;
set @id=0;
SELECT GROUP_CONCAT(id) into @id FROM jsonInternalrelation where idJsonInternalrelationParent in (@ff);
 set @ConcatedString = concat(@ConcatedString, ',', @id);
myloop: WHILE @ff is not null DO

   
    SET @query2 = CONCAT ('SELECT GROUP_CONCAT(idChartProperty) into @ff FROM jsonInternalrelation where idJsonInternalrelationParent in (', @ff, ')');
	PREPARE stmt FROM @query2;
	EXECUTE stmt;
    SET @query2 = CONCAT ('SELECT GROUP_CONCAT(id) into @id FROM jsonInternalrelation where idJsonInternalrelationParent in (', @ff, ')');
	PREPARE stmt FROM @query2;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
IF @id is null THEN
      LEAVE myloop;
end if;
  set @ConcatedString = concat(@ConcatedString, ',', cast(ifnull(@id,0) as char));

 --  set @ConcatedString = concat(@ConcatedString, ',', cast(@id as char));
END WHILE;
SELECT GROUP_CONCAT(id) into @id FROM jsonInternalrelation where idChartProperty in (V);
 set @ConcatedString = concat(@ConcatedString, ',', @id);
 SET @query2 = CONCAT ('SELECT ch.idJsonInternalrelationParent,ch.idChartProperty,ch.value,prop.propertyName,prop.propertyType FROM jsonInternalrelation ch left join jsonproperty prop on ch.idChartProperty=prop.id where ch.id  in (', @ConcatedString, ')');
PREPARE stmt FROM @query2;
	EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END
