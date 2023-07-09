Drop database if exists DBSportStore;
Create database DBSportStore;

Use DBSportStore;

Create table Marca(
	codigoMarca int not null auto_increment,
    nombreMarca varchar(255) not null,
    numeroContacto varchar(8) not null,
    correoContacto varchar(255) not null,
    primary key Pk_codigoMarca (codigoMarca)
);


Create table TipoProducto(
	codigoTProducto int not null auto_increment,
    descripcion varchar(255) not null,
    genero varchar(45),
    primary key Pk_codigoTProducto (codigoTProducto)
);

Create table Talla(
	codigoTalla int not null auto_increment,
    noTalla varchar(45) not null,
    primary key Pk_codigoTalla (codigoTalla)
);

Create table FormaDePago(
	codigoFormaDePago int not null auto_increment,
    formaDePago varchar(100) not null,
    primary key Pk_codigoFormaDePago (codigoFormaDePago)
);

Create table Departamento(
	codigoDepartamento int not null auto_increment,
    departamento varchar(100) not null,
    municipio varchar(100) not null,
    primary key Pk_codigoDepartamento (codigoDepartamento)
);

Create table TipoUsuario(
	codigoTUsuario int not null auto_increment,
    tipoUsuario varchar(255) not null,
    primary key Pk_codigoTUsuario (codigoTUsuario)
);

Create table Usuario(
	codigoUsuario int not null auto_increment,
    nombreUsuario varchar(255) not null,
    apellidoUsuario varchar(255) not null,
    NIT varchar(10) not null,
    correoElectronico varchar(100) not null,
    usuario varchar(50) not null,
    passwordUser varchar(50) not null,
    codigoTUsuario int not null,
    codigoDepartamento int not null,
    primary key Pk_codigoUsuario (codigoUsuario),
    constraint Fk_Usuario_TipoUsuario foreign key (codigoTUsuario)
		references TipoUsuario(codigoTUsuario),
	constraint Fk_Usuario_Departamento foreign key (codigoDepartamento)
		references Departamento(codigoDepartamento)
);

Create table Producto(
	codigoProducto int not null auto_increment,
    nombreProducto varchar(255) not null,
    stock int not null,
    precio double not null,
    codigoMarca int not null,
    codigoTalla int,
    codigoTProducto int not null,
    primary key Pk_codigoProducto (codigoProducto),
    constraint Fk_Producto_Marca foreign key (codigoMarca)
		references Marca(codigoMarca),
	constraint Fk_Producto_Talla foreign key (codigoTalla)
		references Talla (codigoTalla),
	constraint Fk_Producto_TipoProducto foreign key (codigoTProducto)
		references TipoProducto(codigoTProducto)
);

Create table Factura(
	codigoFactura int not null auto_increment,
    fechaFactura varchar(100) not null,
    estado int not null,
    codigoFormaDePago int not null,
    codigoUsuario int not null,
    primary key Pk_codigoFactura (codigoFactura),
    constraint Fk_Factura_FormaDePago foreign key (codigoFormaDePago)
		references FormaDePago(codigoFormaDePago),
	constraint Fk_Factura_Usuario foreign key (codigoUsuario)
		references Usuario(codigoUsuario)
);

Create table DetalleFactura(
	codigoDFactura int not null auto_increment,
    codigoProducto int not null,
    codigoFactura int not null,
    primary key Pk_codigoDFactura (codigoDFactura),
    constraint Fk_DetalleFactura_Producto foreign key (codigoProducto)
		references Producto(codigoProducto),
	constraint Fk_DetalleFactura_Factura foreign key (codigoFactura)
		references Factura(codigoFactura)
);


Insert into Marca (nombreMarca, numeroContacto, correoContacto) values ('Nike', '52634125','estevan123maldrino@gmail.com');
Insert into Marca (nombreMarca, numeroContacto, correoContacto) values ('Adidas', '52364125','garcia15@gmail.com');
Insert into Marca (nombreMarca, numeroContacto, correoContacto) values ('Skechers', '25361425','fernandoEspaña@gmail.com');
Insert into Marca (nombreMarca, numeroContacto, correoContacto) values ('Reebok', '11225656','carlos@gmail.com');
Insert into Marca (nombreMarca, numeroContacto, correoContacto) values ('Fila', '96142563','marcosTulio@gmail.com');



Insert into Talla (noTalla) values ('S');
Insert into Talla (noTalla) values ('M');
Insert into Talla (noTalla) values ('L');
Insert into Talla (noTalla) values ('Xl');
Insert into Talla (noTalla) values ('XXL');

Insert into FormaDePago (formaDePago) values ('Paypal');
Insert into FormaDePago (formaDePago) values ('ContraEntrega');
Insert into FormaDePago (formaDePago) values ('MasterCard');
Insert into FormaDePago (formaDePago) values ('Visa');
Insert into FormaDePago (formaDePago) values ('Bitcoin');

Insert into TipoProducto (descripcion, genero) values ('Algondon', 'Masculino');
Insert into TipoProducto (descripcion, genero) values ('Poliester', 'Femenino');
Insert into TipoProducto (descripcion, genero) values ('Lana', 'Masculino');
Insert into TipoProducto (descripcion, genero) values ('Latex', 'Masculino');
Insert into TipoProducto (descripcion, genero) values ('Cuero', 'UniSex');

Insert into Departamento ( departamento, municipio ) values ('Guatemala', 'Mixco');
Insert into Departamento ( departamento, municipio ) values ('Petén', 'Flores Petén');
Insert into Departamento ( departamento, municipio) values ('Guatemala', 'Villa Nueva');
Insert into Departamento ( departamento, municipio) values ('Chimaltenango', 'Patzun');
Insert into Departamento ( departamento, municipio) values ('Zacapa', 'Gualan');

Insert into TipoUsuario (tipoUsuario) values ('Master');
Insert into TipoUsuario (tipoUsuario) values ('Trabajador');
Insert into TipoUsuario (tipoUsuario) values ('Cliente');

Insert into Usuario (nombreUsuario, apellidoUsuario, NIT, correoElectronico, usuario, passwordUser, codigoTUsuario, codigoDepartamento)
    values ('Carlos', 'lopez', '1234567890',  'carloslop@gmail.com', 'clopez', '123', 3,4);
Insert into Usuario (nombreUsuario, apellidoUsuario, NIT, usuario, passwordUser, correoElectronico, codigoTUsuario, codigoDepartamento)
    values ('Juan', 'Castillo', '1234567890', 'juancastillo@gmail.com','jcastillo', '123',3,2);   
Insert into Usuario (nombreUsuario, apellidoUsuario, NIT, usuario, passwordUser, correoElectronico, codigoTUsuario, codigoDepartamento)
    values ('Adilson', 'Cucul', '1452369887','adilsoncucul@gmail.com', 'ecucul', '54321', 3,5);  
Insert into Usuario (nombreUsuario, apellidoUsuario, NIT, usuario, passwordUser, correoElectronico, codigoTUsuario, codigoDepartamento)
    values ('Gabriela', 'Pinzon', '25361425',  'gabrielapinzon@gmail.com','gpinzon', '987654', 2,1);     
Insert into Usuario (nombreUsuario, apellidoUsuario, NIT, usuario, passwordUser, correoElectronico, codigoTUsuario, codigoDepartamento)
    values ('Stuart', 'Pinzon', '8754219632',  'stuartpinzon@gmail.com','spinzon', '12345', 1,3);  






Insert into Factura(fechaFactura, estado,codigoFormaDePago, codigoUsuario)
    values ('13-03-2022', 1, 1, 1);
Insert into Factura(fechaFactura, estado, codigoFormaDePago, codigoUsuario)
    values ('15-04-2022', 1, 2, 2);
Insert into Factura(fechaFactura, estado, codigoFormaDePago, codigoUsuario)
    values ('20-09-2022', 1, 4, 1);
Insert into Factura(fechaFactura, estado,codigoFormaDePago, codigoUsuario)
    values ('18-09-2022', 1, 3, 1);
Insert into Factura(fechaFactura, estado,codigoFormaDePago, codigoUsuario)
    values ('30-09-2022', 1, 5, 3);

Insert into Producto(nombreProducto, stock, precio, codigoMarca, codigoTalla, codigoTProducto) 
	values('Camisa',10,10.00,1,1,1);
Insert into Producto(nombreProducto, stock, precio, codigoMarca,codigoTProducto) 
	values('Pelota',15,50.00,2,2);
Insert into Producto(nombreProducto, stock, precio, codigoMarca, codigoTalla, codigoTProducto) 
	values('Short',20,25.00,3,3,3);
Insert into Producto(nombreProducto, stock, precio, codigoMarca, codigoTalla, codigoTProducto) 
	values('Licra',50,30.00,4,4,4);
Insert into Producto(nombreProducto, stock, precio, codigoMarca, codigoTalla, codigoTProducto) 
	values('Tenis',30,200.00,5,5,5);



Insert into DetalleFactura (codigoProducto, codigoFactura) values (1,1);
Insert into DetalleFactura (codigoProducto, codigoFactura) values (2,2);
Insert into DetalleFactura (codigoProducto, codigoFactura) values (3,3);
Insert into DetalleFactura (codigoProducto, codigoFactura) values (4,4);
Insert into DetalleFactura (codigoProducto, codigoFactura) values (5,5);

