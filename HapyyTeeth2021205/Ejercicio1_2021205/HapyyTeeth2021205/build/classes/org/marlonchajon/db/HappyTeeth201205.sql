/*
	Nombre: Marlon David Chajon Suchite
    Codigo Tecnico: IN5AV
    Carné: 2021205
    Fecha de Creacion: 26/04/2022
    Fecha de Modificacion: 
*/

Drop database if exists DBHappyTeeth201205;
Create database  DBHappyTeeth201205;

Use DBHappyTeeth201205;

Create table Pacientes(
	codigoPaciente int not null,
    nombresPaciente varchar(50) not null,
    apellidosPaciente varchar(50) not null,
    sexo char not null,
    fechaNacimiento date not null,
    direccionPaciente varchar(100) not null,
    telefonoPersonal varchar(8) not null,
    fechaPrimeraCita date,
    primary key Pk_codigoPaciente (codigoPaciente)
);

Create table Especialidades(
	codigoEspecialidad int not null auto_increment,
    descripcion varchar(100) not null,
    primary key PK_codigoEspecialidad (codigoEspecialidad)
);

Create table Medicamentos(
	codigoMedicamento int not null auto_increment,
    nombreMedicamento varchar(100) not null,
    primary key PK_codigoMedicamento (codigoMedicamento)
);

Create table Doctores(
	numeroColegiado int not null,
    nombresDoctor varchar(50) not null,
    apellidosDoctor varchar(50) not null,
    telefonoContacto varchar(8) not null,
    codigoEspecialidad int not null,
    primary key PK_numeroColegiado (numeroColegiado),
    constraint FK_Doctores_Especialidades foreign key (codigoEspecialidad)
		references Especialidades (codigoEspecialidad)
);

Create table Recetas(
	codigoReceta int not null auto_increment,
    fechaReceta date not null,
    numeroColegiado int not null,
    primary key PK_codigoRecetas (codigoReceta),
    constraint FK_Recetas_Doctores foreign key (numeroColegiado)
		references Doctores (numeroColegiado)
);

Create table DetalleReceta(
	codigoDetalleReceta int not null auto_increment,
    dosis varchar(100) not null,
    codigoReceta int not null,
    codigoMedicamento int not null,
    primary key PK_codigoDetalleReceta (codigoDetalleReceta),
    constraint FK_DetalleReceta_Recetas foreign key (codigoReceta)
		references Recetas (codigoReceta),
	constraint FK_DetalleReceta_Medicamentos foreign key (codigoMedicamento)
		references Medicamentos (codigoMedicamento)
);

Create table Citas(
	codigoCita int not null auto_increment,
    fechaCita date not null,
    horaCita time not null,
    tratamiento varchar(150),
    descripCondActual varchar(255) not null,
    codigoPaciente int not null,
    numeroColegiado int not null,
    primary key PK_codigoCita (codigoCita),
    constraint FK_Citas_Pacientes foreign key (codigoPaciente)
		references Pacientes (codigoPaciente),
	constraint FK_Citas_Doctores foreign key (numeroColegiado)
		references Doctores (numeroColegiado)
);



-- ************************************PROCEDIMIENTOS ALMACENADOS**********************************************
-- -----------------------------Procedimientos Almacenados Pacientes-----------------------------------
-- --------------------------------------Agregar Pacientes--------------------------------------
Delimiter $$
	Create procedure sp_AgregarPaciente(in codigoPaciente int, in nombresPaciente varchar(50),
		in apellidosPaciente varchar(150), in sexo char, in fechaNacimiento date,
        in direccionPaciente varchar(100), in telefonoPersonal varchar(8), in fechaPrimeraCita date)
        Begin
			Insert into Pacientes (codigoPaciente, nombresPaciente, apellidosPaciente, sexo,
					fechaNacimiento, direccionPaciente, telefonoPersonal, fechaPrimeraCita)
					values (codigoPaciente, nombresPaciente, apellidosPaciente, sexo,
					fechaNacimiento, direccionPaciente, telefonoPersonal, fechaPrimeraCita);
        End$$
Delimiter ;

Call sp_AgregarPaciente(1,"Marcos","Chajon","M",'2004-12-22',"3ra calle lote a 37",4435455,now());
Call sp_AgregarPaciente(2,"Estiben","Flores","M",'2000-06-01',"5ta avenia colonia Landivar",4435455,now());

-- --------------------------------Listar Pacientes--------------------------------------
Delimiter $$
	Create procedure sp_ListarPacientes()
		Begin
			select
				P.codigoPaciente,
                P.nombresPaciente,
                P.apellidosPaciente,
                P.sexo,
                P.fechaNacimiento,
                P.direccionPaciente,
                P.telefonoPersonal,
                P.fechaPrimeraCita
                from Pacientes P;
        End$$
Delimiter ;

Call sp_ListarPacientes();

-- -------------------------Buscar Pacientes------------------------------------
Delimiter $$
	Create procedure sp_BuscarPaciente(in codPaciente int)
		Begin
			select
				P.codigoPaciente,
                P.nombresPaciente,
                P.apellidosPaciente,
                P.sexo,
                P.fechaNacimiento,
                P.direccionPaciente,
                P.telefonoPersonal,
                P.fechaPrimeraCita
                from Pacientes P
                where codigoPaciente = codPaciente;
        End$$
Delimiter ;

call sp_BuscarPaciente(1);

-- --------------------------Eliminar Pacientes------------------------------------
Delimiter $$
	Create procedure sp_EliminarPaciente(in codPaciente int)
		Begin
			Delete from Pacientes
				where codigoPaciente = codPaciente;
        End$$
Delimiter ;

call sp_EliminarPaciente(1);
call sp_ListarPacientes();

-- ----------------------Editar Pacientes------------------------------
Delimiter $$
	Create procedure sp_EditarPaciente(in codigoPaciente int, in nomPaciente varchar(50),
    in apellidosPaciente varchar(50), in sx char, in fechaNac date, in direccionPaciente varchar(100),
    in telefonoPersonal varchar(8), in fechaPC date)
		Begin
			Update Pacientes P
				set
					P.nombresPaciente = nomPaciente,
                    P.apellidosPaciente = apePaciente,
                    P.sexo = sx,
                    P.fechaNacimiento = fechaNac,
					P.direccionPaciente = dirPaciente,
                    P.telefonoPersonal = telPersonal,
                    P.fechaPrimeraCita = fechaPC
                    where codigoPaciente = codPaciente;
                    
        End$$
Delimiter ;


-- *******************************Procedimientos Almacenados De Especialidades***********************************
-- -----------------------------------------Agregar Especialidad------------------------------------
Delimiter $$
	Create procedure sp_AgregarEspecialidad(in descripcion varchar(100))
		Begin
			Insert into Especialidades (descripcion)
				Values(descripcion);
        End$$
Delimiter ;

Call sp_AgregarEspecialidad("Cirujado Nueronal");
Call sp_AgregarEspecialidad("Limpiesa vocal");

-- --------------------------------------Listar Especialidad------------------------------------------
Delimiter $$
	Create procedure sp_ListarEspecialidades()
		Begin
			Select
				E.codigoEspecialidad,
                E.descripcion
				From Especialidades E;
        End$$
Delimiter ;

Call sp_ListarEspecialidades();

-- --------------------------------------Buscar Epecialidad-----------------------------------------
Delimiter $$
	Create procedure sp_BuscarEspecialidad(in codEspecialidad int)
		Begin
			Select
				E.codigoEspecialidad,
                E.descripcion
                from Especialidades E
                Where codigoEspecialidad = codEspecialidad;
        End$$
Delimiter ;

Call sp_BuscarEspecialidad(1);

-- --------------------------------------Eliminar Especialidad---------------------------------
Delimiter $$
	Create procedure sp_EliminarEspecialidad(in codEspecialidad int)
		Begin
			Delete from Especialidades 
				where codigoEspecialidad = codEspecialidad;
        End$$
Delimiter ;

Call sp_EliminarEspecialidad(1);
Call sp_ListarEspecialidades();

-- -----------------------------Editar Especialidad---------------------------
Delimiter $$
	Create procedure sp_EditarEspecialidad(in codEspecialidad int, in descrip varchar(100))
		Begin
			Update Especialidades E
				set 
					E.descripcion = descrip
					where codigoEspecialidad = codEspecialidad;
				
        End$$
Delimiter ;


-- *******************************Procedimientos Almacenados De Medicamentos*********************************
-- ---------------------------------------Agregar Medicamentos---------------------------------------
Delimiter $$
	Create procedure sp_AgregarMedicamento(in nombreMedicamento varchar(100))
		Begin
			Insert Into Medicamentos(nombreMedicamento)
				Values(nombreMedicamento);
        End$$
Delimiter ;

Call sp_AgregarMedicamento("Clodifenaco");
Call sp_AgregarMedicamento("Asetaminofen");

-- ---------------------------------------Listar Medicamentos------------------------------------
Delimiter $$
	Create procedure sp_ListarMedicamentos()
		Begin
			Select
				M.codigoMedicamento,
                M.nombreMedicamento
				From Medicamentos M;
        End$$
Delimiter ;

Call sp_ListarMedicamentos();

-- ---------------------------------------Buscar Medicamentos---------------------------------------
Delimiter $$
	Create procedure sp_BuscarMedicamento(in codMedicamento int)
		Begin
			Select 
				M.codigoMedicamento,
                M.nombreMedicamento
                From Medicamentos M
                Where codigoMedicamento = codMedicamento;
        End$$
Delimiter ;

Call sp_BuscarMedicamento(1);

-- -------------------------------------Elimiter Medicamentos----------------------------------------
Delimiter $$
	Create procedure sp_EliminarMedicamento(in codMedicamento int)
		Begin
			Delete from Medicamentos 
				Where codigoMedicamento = codMedicamento;
        End$$
Delimiter ;

Call sp_EliminarMedicamento(1);
Call sp_ListarMedicamentos();

-- ------------------------------------Editar Medicamento-----------------------------------------
Delimiter $$
	Create procedure sp_EditarMedicamento(in codMedicamento int, in nomMedicamento varchar(100))
		Begin
			Update Medicamentos M
				set
					M.nombreMedicamento = nomMedicamento
                    where codigoMedicamento = codMedicamento;
        End$$
Delimiter ;



-- **************************************Procedimientos Almacenados De Doctores***********************************
-- ----------------------------------------------Agregar Doctores------------------------------------------------
Delimiter $$
	Create procedure sp_AgregarDoctor(in numeroColegiado int , in nombresDoctor varchar(50), in apellidosDoctor varchar(50),
		in telefonoContacto varchar(8), in codigoEspecialidad int)
        Begin
			Insert Into Doctores(numeroColegiado, nombresDoctor, apellidosDoctor, telefonoContacto, codigoEspecialidad)
				Values(numeroColegiado, nombresDoctor, apellidosDoctor, telefonoContacto, codigoEspecialidad);
        End$$
								
Delimiter ;

Call sp_AgregarDoctor(1002,"Eduardo","Ramirez","51062481",2);
Call sp_AgregarDoctor(1003,"Fernando","Chajon","52064352",2);

-- ----------------------------------------Listar Doctores-------------------------------------
Delimiter $$
	Create procedure sp_ListarDoctores()
		Begin
			Select
				D.numeroColegiado,
                D.nombresDoctor,
                D.apellidosDoctor,
                D.telefonoContacto,
                D.CodigoEspecialidad
                from Doctores D;
        End$$
Delimiter ;

Call sp_ListarDoctores();

-- -----------------------------------------------Buscar Doctores----------------------------------
Delimiter $$
	Create procedure sp_BuscarDoctor(in numColegiado int)
		Begin
			Select
				D.numeroColegiado,
                D.nombresDoctor,
                D.apellidosDoctor,
                D.telefonoContacto,
                D.codigoEspecialidad
                From Doctores D 
                where numeroColegiado = numColegiado;
        End$$
Delimiter ;

Call sp_BuscarDoctor(1002);

-- ------------------------------Eliminar Doctores---------------------------------------
Delimiter $$
	Create procedure sp_EliminarDoctor(in numColegiado int)
		Begin
			Delete from Doctores 
				Where numeroColegiado = numColegiado;
        End$$
Delimiter ;

Call sp_EliminarDoctor(1002);
Call sp_ListarDoctores();

-- -----------------------------Editar Doctores--------------------------------------------
Delimiter $$
	Create procedure sp_EditarDoctor(in numColegiado int, in nomDoctor varchar(50), in apeDoctor varchar(50), 
		in telContacto varchar(8), in codEspecialidad int)
		Begin
			Update Doctores D 
				set
					D.nombresDoctor = nomDoctor,
                    D.apellidosDoctor = apeDoctor,
                    D.telefonoContacto = telContacto,
                    D.codigoEspecialidad = codEspecialidad
                    where numeroColegiado = numColegiado;
        End$$
Delimiter ;


-- *********************************Procedimientos Almacenados de Recetas*********************
-- ----------------------------------------Agregar Recetas---------------------------------
Delimiter $$
	Create procedure sp_AgregarReceta(in fechaReceta Date, in numeroColegiado int)
		Begin
			Insert Into Recetas(fechaReceta, numeroColegiado)
				 Values(fechaReceta, numeroColegiado);
        End$$
Delimiter ;

Call sp_AgregarReceta('2010-02-12',1003);
Call sp_AgregarReceta('2011-04-01',1003);

-- --------------------------------------------Listar Recetas-------------------------------
Delimiter $$
	Create procedure sp_ListarRecetas()
		Begin
			Select
				R.codigoReceta,
				R.fechaReceta,
                R.numeroColegiado
                From Recetas R;
        End$$
Delimiter ;

Call sp_ListarRecetas();

-- ---------------------------------------Buscar Recetas--------------------------------
Delimiter $$
	Create procedure sp_BuscarReceta(in codReceta int)
		Begin	
			Select
				R.codigoReceta,
                R.fechaReceta,
                R.numeroColegiado
                From Recetas R
                Where codigoReceta = codReceta;
        End$$
Delimiter ;

Call sp_BuscarReceta(1);

-- -----------------------------------------Eliminar Receta-----------------------------------------
Delimiter $$
	Create procedure sp_EliminarReceta(in codReceta int)
		Begin
			Delete from Recetas
				Where codigoReceta = codReceta;
        End$$
Delimiter ;

Call sp_EliminarReceta(1);
Call sp_ListarRecetas();

-- ---------------------------Editar Receta ----------------------------------
Delimiter $$
	Create procedure sp_EditarReceta(in codReceta int, in fechaReceta Date, in numColegiado int)
		Begin
			Update Recetas R
				set
					R.fechaReceta = fechaReceta,
                    R.numeroColegiado = numColegiado
                    where codigoReceta = codReceta;
        End$$
Delimiter ;


-- *************************************Procedimientos Almacenados De DetalleReceta**************************************
-- -----------------------------------------------Agregar DetalleReceta----------------------------
Delimiter $$
	Create procedure sp_AgregarDetalleReceta(in dosis varchar(100), in codigoReceta int , in codigoMedicamento int)
		Begin
			Insert Into DetalleReceta(dosis, codigoReceta, codigoMedicamento)
				Values(dosis, codigoReceta, codigoMedicamento);
        End$$
Delimiter ;

Call sp_AgregarDetalleReceta("1 dosis por dia",2,2);
Call sp_AgregarDetalleReceta("1 cada 8 horas",2,2);

-- -----------------------------------------------Listar DetalleReceta-----------------------------
Delimiter $$
	Create procedure sp_ListarDetalleRecetas()
		Begin
			Select
				DR.codigoDetalleReceta,
                DR.dosis,
                DR.codigoReceta,
                DR.codigoMedicamento
                From DetalleReceta DR;
        End$$
Delimiter ;

Call sp_ListarDetalleRecetas();

-- -------------------------------Buscar DetalleReceta---------------------------------------
Delimiter $$
	Create procedure sp_BuscarDetalleReceta(in codDetalleReceta int)
		Begin
			Select
				DR.codigoDetalleReceta,
                DR.dosis,
                DR.codigoReceta,
                DR.codigoMedicamento
                From DetalleReceta DR
                Where codigoDetalleReceta = codDetalleReceta;
        End$$
Delimiter ;

Call sp_BuscarDetalleReceta(2);

-- -----------------------Eliminar DetalleReceta--------------------------------
Delimiter $$
	Create procedure sp_EliminarDetalleReceta(in codDetalleReceta int)
		Begin
			Delete From DetalleReceta
				Where codigoDetalleReceta = codDetalleReceta;
        End$$
Delimiter ;

Call sp_EliminarDetalleReceta(1);

-- -----------------------------Editar DetalleReceta-------------------------------
Delimiter $$
	Create procedure sp_EditarDetalleReceta(in codDetalleReceta int, in dosis varchar(100), in codReceta int, in codMedicamento int)
		Begin
			Update DetalleReceta DR
				set
					DR.dosis = dosis,
                    DR.codigoReceta = codReceta,
                    DR.codigoMedicamento = codMedicamento
                    Where codigoDetalleReceta = codDetalleReceta;
        End$$
Delimiter ;


-- ************************************Procedimientos Almacenados De Citas*****************************************
-- ------------------------------------------Agregar Citas-------------------------------------------------------
Delimiter $$
	Create procedure sp_AgregarCita(in fechaCita Date, in horaCita Time, in tratamiento varchar(150), in descripCondActual varchar(255),
		in codigoPaciente int, in numeroColegiado int)
        Begin
			Insert Into Citas(fechaCita, horaCita, tratamiento, descripCondActual, codigoPaciente, numeroColegiado)
				Values(fechaCita, horaCita, tratamiento, descripCondActual, codigoPaciente, numeroColegiado);
        End$$
Delimiter ;	
	
Call sp_AgregarCita('2010-02-04','10:30 ',"Limpieza","Nescesita una limpieza profunda",2,1003);
Call sp_AgregarCita('2010-02-04','11:30 ',"Tratamiento de Canales","Tine muy sensibles sus encillas",2,1003);

-- --------------------------------------Listar Citas-----------------------------------
Delimiter $$
	Create procedure sp_ListarCitas()
		Begin
			Select
				C.codigoCita,
                C.fechaCita,
                C.horaCita,
                C.tratamiento,
                C.descripCondActual,
                C.codigoPaciente,
                C.numeroColegiado
                From Citas C;
        End$$
Delimiter ;

Call sp_ListarCitas();

-- ---------------------------Buscar Cita-----------------------
Delimiter $$
	Create procedure sp_BuscarCita(in codCita int)
		Begin
			Select
				C.codigoCita,
                C.fechaCita,
                C.horaCita,
                C.tratamiento,
                C.descripCondActual,
                C.codigoPaciente,
                C.numeroColegiado
                From Citas C
                Where codigoCita = codCita;
        End$$
Delimiter ;

Call sp_BuscarCita(1);

-- -----------------------------Elimiar Cita-------------------------
Delimiter $$
	Create procedure sp_EliminarCita(in codCita int)
		Begin
			Delete From Citas
				Where codigoCita = codCita;
        End$$
Delimiter ;

Call sp_EliminarCita(1);
Call sp_ListarCitas();

-- ----------------------------------Editar Cita-------------------------------
Delimiter $$
	Create procedure sp_EditarCita(in codCita int, in fechaCita Date, in horaCita Time, in tratamiento varchar(150), in descripCondActual varchar(255),
		in codPaciente int, in numColegiado int)
		Begin
			Update Citas C 
				set 
					C.fechaCita = fechaCita,
                    C.horaCita = horaCita,
                    C.tratamiento = tratamiento,
                    C.descripConActual = descripConActual,
                    C.codigoPaciente = codPaciente,
                    C.numeroColegiado = numColegiado
                    Where codigoCita = codCita;
        End$$
Delimiter ;

