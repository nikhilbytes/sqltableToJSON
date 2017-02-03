CREATE TABLE `jsonInternalrelation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idJsonproperty` int(11) DEFAULT NULL,
  `idJsonpropertyParent` int(11) DEFAULT NULL,
  `value` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idchartprop_idx` (`idJsonproperty`,`idJsonInternalrelation`)
) ENGINE=InnoDB AUTO_INCREMENT=621 DEFAULT CHARSET=latin1;


CREATE TABLE `jsonproperty` (
  `id` int(11) NOT NULL,
  `propertyName` varchar(100) DEFAULT NULL,
  `propertyType` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
