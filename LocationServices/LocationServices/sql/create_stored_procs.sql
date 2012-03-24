delimiter //

DROP FUNCTION IF EXISTS `diners`.`GetDistance`
//
CREATE DEFINER=`tom`@`localhost` FUNCTION `GetDistance`(orig_lat DOUBLE, orig_lon DOUBLE, dest_lat DOUBLE, dest_lon DOUBLE) RETURNS int(11)
    DETERMINISTIC
BEGIN
     DECLARE dist INT;
     SET dist = ROUND( 3956 * 2 * ASIN(SQRT(
POWER(SIN((abs(orig_lat) - abs(dest_lat)) * pi()/180 / 2),
2) + COS(abs(orig_lat) * pi()/180 ) * COS(abs(dest_lat) *
pi()/180) * POWER(SIN((abs(orig_lon) - abs(dest_lon)) *
pi()/180 / 2), 2) )) );
     RETURN dist;
    END
//



delimiter //

DROP PROCEDURE IF EXISTS `diners`.`GetAllDiners`
//
CREATE DEFINER=`tom`@`localhost` PROCEDURE `GetAllDiners`()
BEGIN
SELECT * FROM DINERS;
END
//


delimiter //

DROP PROCEDURE IF EXISTS `diners`.`GetDinersWithinRadius`
//
CREATE DEFINER=`tom`@`localhost` PROCEDURE `GetDinersWithinRadius`(IN originLat double, IN originLon double, IN dist int)
BEGIN

SELECT *, GetDistance(originLat, originLon, dest.latitude, dest.longitude)  as distance
FROM diners dest
having distance < dist
ORDER BY distance;

END
//




