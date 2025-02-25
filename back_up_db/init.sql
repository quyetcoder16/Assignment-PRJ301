USE [master]
GO
/****** Object:  Database [leave_management]    Script Date: 2/21/2025 9:14:40 AM ******/
CREATE DATABASE [leave_management]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'leave_management', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.SQLEXPRESS\MSSQL\DATA\leave_management.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'leave_management_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.SQLEXPRESS\MSSQL\DATA\leave_management_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT, LEDGER = OFF
GO
ALTER DATABASE [leave_management] SET COMPATIBILITY_LEVEL = 160
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [leave_management].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [leave_management] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [leave_management] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [leave_management] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [leave_management] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [leave_management] SET ARITHABORT OFF 
GO
ALTER DATABASE [leave_management] SET AUTO_CLOSE ON 
GO
ALTER DATABASE [leave_management] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [leave_management] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [leave_management] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [leave_management] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [leave_management] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [leave_management] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [leave_management] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [leave_management] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [leave_management] SET  DISABLE_BROKER 
GO
ALTER DATABASE [leave_management] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [leave_management] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [leave_management] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [leave_management] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [leave_management] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [leave_management] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [leave_management] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [leave_management] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [leave_management] SET  MULTI_USER 
GO
ALTER DATABASE [leave_management] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [leave_management] SET DB_CHAINING OFF 
GO
ALTER DATABASE [leave_management] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [leave_management] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [leave_management] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [leave_management] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
ALTER DATABASE [leave_management] SET QUERY_STORE = ON
GO
ALTER DATABASE [leave_management] SET QUERY_STORE (OPERATION_MODE = READ_WRITE, CLEANUP_POLICY = (STALE_QUERY_THRESHOLD_DAYS = 30), DATA_FLUSH_INTERVAL_SECONDS = 900, INTERVAL_LENGTH_MINUTES = 60, MAX_STORAGE_SIZE_MB = 1000, QUERY_CAPTURE_MODE = AUTO, SIZE_BASED_CLEANUP_MODE = AUTO, MAX_PLANS_PER_QUERY = 200, WAIT_STATS_CAPTURE_MODE = ON)
GO
USE [leave_management]
GO
/****** Object:  Table [dbo].[department]    Script Date: 2/21/2025 9:14:40 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[department](
	[dep_id] [int] NOT NULL,
	[manager_id] [bigint] NULL,
	[dep_name] [nvarchar](255) NULL,
	[description] [nvarchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[dep_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[invalidated_token]    Script Date: 2/21/2025 9:14:40 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[invalidated_token](
	[expiry_time] [datetime2](6) NOT NULL,
	[id_token] [varchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id_token] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[permission]    Script Date: 2/21/2025 9:14:40 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[permission](
	[description] [nvarchar](1000) NULL,
	[permission_name] [varchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[permission_name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[request_leave]    Script Date: 2/21/2025 9:14:40 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[request_leave](
	[from_date] [date] NOT NULL,
	[id_request] [int] IDENTITY(1,1) NOT NULL,
	[status_id] [int] NOT NULL,
	[to_date] [date] NOT NULL,
	[type_leave_id] [int] NULL,
	[user_created_id] [bigint] NOT NULL,
	[user_process_id] [bigint] NULL,
	[note_process] [nvarchar](1000) NULL,
	[reason] [nvarchar](1000) NULL,
	[title] [nvarchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[id_request] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[request_status]    Script Date: 2/21/2025 9:14:40 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[request_status](
	[status_id] [int] IDENTITY(1,1) NOT NULL,
	[description] [nvarchar](500) NULL,
	[status_name] [nvarchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[status_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[role]    Script Date: 2/21/2025 9:14:40 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[role](
	[description] [nvarchar](255) NULL,
	[role_name] [varchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[role_name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[role_permission]    Script Date: 2/21/2025 9:14:40 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[role_permission](
	[permission_name] [varchar](255) NOT NULL,
	[role_name] [varchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[permission_name] ASC,
	[role_name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[type_leave]    Script Date: 2/21/2025 9:14:40 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[type_leave](
	[type_leave_id] [int] IDENTITY(1,1) NOT NULL,
	[description] [nvarchar](255) NULL,
	[name_type_leave] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[type_leave_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[user_role]    Script Date: 2/21/2025 9:14:40 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[user_role](
	[user_id] [bigint] NOT NULL,
	[role_name] [varchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[user_id] ASC,
	[role_name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[users]    Script Date: 2/21/2025 9:14:40 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[users](
	[dep_id] [int] NULL,
	[superior_id] [bigint] NULL,
	[user_id] [bigint] IDENTITY(1,1) NOT NULL,
	[email] [varchar](255) NOT NULL,
	[full_name] [nvarchar](255) NULL,
	[password] [varchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[user_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
INSERT [dbo].[department] ([dep_id], [manager_id], [dep_name], [description]) VALUES (1, 7, N'IT', N'Phòng ban công nghệ thông tin')
INSERT [dbo].[department] ([dep_id], [manager_id], [dep_name], [description]) VALUES (2, 9, N'QA', N'Phòng ban kiểm thử chất lượng')
INSERT [dbo].[department] ([dep_id], [manager_id], [dep_name], [description]) VALUES (3, 10, N'Sales', N'Phòng ban bán hàng')
GO
INSERT [dbo].[permission] ([description], [permission_name]) VALUES (N'Approving applications from subordinates', N'APPOVAL_REQUEST')
INSERT [dbo].[permission] ([description], [permission_name]) VALUES (N'create my request', N'CREATED_MY_REQUEST')
INSERT [dbo].[permission] ([description], [permission_name]) VALUES (N'delete my request', N'DELETE_MY_REQUEST')
INSERT [dbo].[permission] ([description], [permission_name]) VALUES (N'edit my request', N'EDIT_MY_REQUEST')
INSERT [dbo].[permission] ([description], [permission_name]) VALUES (N'view agenda of employee', N'VIEW_AGENDA')
INSERT [dbo].[permission] ([description], [permission_name]) VALUES (N'view my request', N'VIEW_MY_REQUEST')
INSERT [dbo].[permission] ([description], [permission_name]) VALUES (N'view subordinate''s application', N'VIEW_SUB_REQUEST')
GO
SET IDENTITY_INSERT [dbo].[request_status] ON 

INSERT [dbo].[request_status] ([status_id], [description], [status_name]) VALUES (1, N'In progress', N'In progress')
INSERT [dbo].[request_status] ([status_id], [description], [status_name]) VALUES (2, N'Approved', N'Approved')
INSERT [dbo].[request_status] ([status_id], [description], [status_name]) VALUES (3, N'Rejected', N'Rejected')
SET IDENTITY_INSERT [dbo].[request_status] OFF
GO
INSERT [dbo].[role] ([description], [role_name]) VALUES (N'company director', N'DIRECTOR')
INSERT [dbo].[role] ([description], [role_name]) VALUES (N'Head of a department', N'DIVISION_LEADER')
INSERT [dbo].[role] ([description], [role_name]) VALUES (N'employee', N'EMPLOYEE')
INSERT [dbo].[role] ([description], [role_name]) VALUES (N'team leader', N'LEADER')
GO
INSERT [dbo].[role_permission] ([permission_name], [role_name]) VALUES (N'APPOVAL_REQUEST', N'DIRECTOR')
INSERT [dbo].[role_permission] ([permission_name], [role_name]) VALUES (N'APPOVAL_REQUEST', N'DIVISION_LEADER')
INSERT [dbo].[role_permission] ([permission_name], [role_name]) VALUES (N'APPOVAL_REQUEST', N'LEADER')
INSERT [dbo].[role_permission] ([permission_name], [role_name]) VALUES (N'CREATED_MY_REQUEST', N'DIVISION_LEADER')
INSERT [dbo].[role_permission] ([permission_name], [role_name]) VALUES (N'CREATED_MY_REQUEST', N'EMPLOYEE')
INSERT [dbo].[role_permission] ([permission_name], [role_name]) VALUES (N'CREATED_MY_REQUEST', N'LEADER')
INSERT [dbo].[role_permission] ([permission_name], [role_name]) VALUES (N'DELETE_MY_REQUEST', N'DIVISION_LEADER')
INSERT [dbo].[role_permission] ([permission_name], [role_name]) VALUES (N'DELETE_MY_REQUEST', N'EMPLOYEE')
INSERT [dbo].[role_permission] ([permission_name], [role_name]) VALUES (N'DELETE_MY_REQUEST', N'LEADER')
INSERT [dbo].[role_permission] ([permission_name], [role_name]) VALUES (N'EDIT_MY_REQUEST', N'DIVISION_LEADER')
INSERT [dbo].[role_permission] ([permission_name], [role_name]) VALUES (N'EDIT_MY_REQUEST', N'EMPLOYEE')
INSERT [dbo].[role_permission] ([permission_name], [role_name]) VALUES (N'EDIT_MY_REQUEST', N'LEADER')
INSERT [dbo].[role_permission] ([permission_name], [role_name]) VALUES (N'VIEW_AGENDA', N'DIRECTOR')
INSERT [dbo].[role_permission] ([permission_name], [role_name]) VALUES (N'VIEW_AGENDA', N'DIVISION_LEADER')
INSERT [dbo].[role_permission] ([permission_name], [role_name]) VALUES (N'VIEW_MY_REQUEST', N'DIVISION_LEADER')
INSERT [dbo].[role_permission] ([permission_name], [role_name]) VALUES (N'VIEW_MY_REQUEST', N'EMPLOYEE')
INSERT [dbo].[role_permission] ([permission_name], [role_name]) VALUES (N'VIEW_MY_REQUEST', N'LEADER')
INSERT [dbo].[role_permission] ([permission_name], [role_name]) VALUES (N'VIEW_SUB_REQUEST', N'DIRECTOR')
INSERT [dbo].[role_permission] ([permission_name], [role_name]) VALUES (N'VIEW_SUB_REQUEST', N'DIVISION_LEADER')
INSERT [dbo].[role_permission] ([permission_name], [role_name]) VALUES (N'VIEW_SUB_REQUEST', N'LEADER')
GO
SET IDENTITY_INSERT [dbo].[type_leave] ON 

INSERT [dbo].[type_leave] ([type_leave_id], [description], [name_type_leave]) VALUES (1, N'Annual leave', N'Annual')
INSERT [dbo].[type_leave] ([type_leave_id], [description], [name_type_leave]) VALUES (2, N'Sick leave', N'Sick')
INSERT [dbo].[type_leave] ([type_leave_id], [description], [name_type_leave]) VALUES (3, N'Maternity leave', N'Maternity')
SET IDENTITY_INSERT [dbo].[type_leave] OFF
GO
INSERT [dbo].[user_role] ([user_id], [role_name]) VALUES (6, N'EMPLOYEE')
INSERT [dbo].[user_role] ([user_id], [role_name]) VALUES (7, N'DIVISION_LEADER')
INSERT [dbo].[user_role] ([user_id], [role_name]) VALUES (8, N'LEADER')
INSERT [dbo].[user_role] ([user_id], [role_name]) VALUES (9, N'DIVISION_LEADER')
INSERT [dbo].[user_role] ([user_id], [role_name]) VALUES (10, N'DIVISION_LEADER')
GO
SET IDENTITY_INSERT [dbo].[users] ON 

INSERT [dbo].[users] ([dep_id], [superior_id], [user_id], [email], [full_name], [password]) VALUES (1, 7, 6, N'sdf', N'dssd', N'sdasd')
INSERT [dbo].[users] ([dep_id], [superior_id], [user_id], [email], [full_name], [password]) VALUES (1, NULL, 7, N'divisionit@prj.com', N'division IT', N'$2y$10$BbX72uUh2SD9ZqsqaW5oG.XygalBQdiwnfcM53TcXogX36yHvjOY2')
INSERT [dbo].[users] ([dep_id], [superior_id], [user_id], [email], [full_name], [password]) VALUES (1, NULL, 8, N'leader@prj.com', N'leader', N'$2y$10$BbX72uUh2SD9ZqsqaW5oG.XygalBQdiwnfcM53TcXogX36yHvjOY2')
INSERT [dbo].[users] ([dep_id], [superior_id], [user_id], [email], [full_name], [password]) VALUES (2, NULL, 9, N'division_qa@prj.com', N'division QA', N'$2y$10$BbX72uUh2SD9ZqsqaW5oG.XygalBQdiwnfcM53TcXogX36yHvjOY2')
INSERT [dbo].[users] ([dep_id], [superior_id], [user_id], [email], [full_name], [password]) VALUES (3, NULL, 10, N'division_sale@prj.com', N'division sale', N'$2y$10$BbX72uUh2SD9ZqsqaW5oG.XygalBQdiwnfcM53TcXogX36yHvjOY2')
SET IDENTITY_INSERT [dbo].[users] OFF
GO
/****** Object:  Index [UKg9435hkqyjp3h3qsaslcmk4rw]    Script Date: 2/21/2025 9:14:41 AM ******/
CREATE UNIQUE NONCLUSTERED INDEX [UKg9435hkqyjp3h3qsaslcmk4rw] ON [dbo].[department]
(
	[manager_id] ASC
)
WHERE ([manager_id] IS NOT NULL)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UK6dotkott2kjsp8vw4d0m25fb7]    Script Date: 2/21/2025 9:14:41 AM ******/
ALTER TABLE [dbo].[users] ADD  CONSTRAINT [UK6dotkott2kjsp8vw4d0m25fb7] UNIQUE NONCLUSTERED 
(
	[email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
ALTER TABLE [dbo].[department]  WITH CHECK ADD  CONSTRAINT [FK4b3j4ilxbfdt9fes1junm2cph] FOREIGN KEY([manager_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[department] CHECK CONSTRAINT [FK4b3j4ilxbfdt9fes1junm2cph]
GO
ALTER TABLE [dbo].[request_leave]  WITH CHECK ADD  CONSTRAINT [FK337d1oh5i8v1upl2t26le7tx8] FOREIGN KEY([user_created_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[request_leave] CHECK CONSTRAINT [FK337d1oh5i8v1upl2t26le7tx8]
GO
ALTER TABLE [dbo].[request_leave]  WITH CHECK ADD  CONSTRAINT [FKdtefiuwgm86ys1d8q0rtyj24k] FOREIGN KEY([user_process_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[request_leave] CHECK CONSTRAINT [FKdtefiuwgm86ys1d8q0rtyj24k]
GO
ALTER TABLE [dbo].[request_leave]  WITH CHECK ADD  CONSTRAINT [FKebn87pi42ofx2dsanx0d7w7ws] FOREIGN KEY([status_id])
REFERENCES [dbo].[request_status] ([status_id])
GO
ALTER TABLE [dbo].[request_leave] CHECK CONSTRAINT [FKebn87pi42ofx2dsanx0d7w7ws]
GO
ALTER TABLE [dbo].[request_leave]  WITH CHECK ADD  CONSTRAINT [FKnxq0vghglisytkwab9l8cljpd] FOREIGN KEY([type_leave_id])
REFERENCES [dbo].[type_leave] ([type_leave_id])
GO
ALTER TABLE [dbo].[request_leave] CHECK CONSTRAINT [FKnxq0vghglisytkwab9l8cljpd]
GO
ALTER TABLE [dbo].[role_permission]  WITH CHECK ADD  CONSTRAINT [FKldkc3yoh80x16gv94519otli4] FOREIGN KEY([permission_name])
REFERENCES [dbo].[permission] ([permission_name])
GO
ALTER TABLE [dbo].[role_permission] CHECK CONSTRAINT [FKldkc3yoh80x16gv94519otli4]
GO
ALTER TABLE [dbo].[role_permission]  WITH CHECK ADD  CONSTRAINT [FKng6lj87lo2uxina096lfjpdlp] FOREIGN KEY([role_name])
REFERENCES [dbo].[role] ([role_name])
GO
ALTER TABLE [dbo].[role_permission] CHECK CONSTRAINT [FKng6lj87lo2uxina096lfjpdlp]
GO
ALTER TABLE [dbo].[user_role]  WITH CHECK ADD  CONSTRAINT [FKj345gk1bovqvfame88rcx7yyx] FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[user_role] CHECK CONSTRAINT [FKj345gk1bovqvfame88rcx7yyx]
GO
ALTER TABLE [dbo].[user_role]  WITH CHECK ADD  CONSTRAINT [FKn6r4465stkbdy93a9p8cw7u24] FOREIGN KEY([role_name])
REFERENCES [dbo].[role] ([role_name])
GO
ALTER TABLE [dbo].[user_role] CHECK CONSTRAINT [FKn6r4465stkbdy93a9p8cw7u24]
GO
ALTER TABLE [dbo].[users]  WITH CHECK ADD  CONSTRAINT [FK4yhbch9efntvnoj7widh4o14q] FOREIGN KEY([dep_id])
REFERENCES [dbo].[department] ([dep_id])
GO
ALTER TABLE [dbo].[users] CHECK CONSTRAINT [FK4yhbch9efntvnoj7widh4o14q]
GO
ALTER TABLE [dbo].[users]  WITH CHECK ADD  CONSTRAINT [FKsh8ndy2iuas3g5iwyr6cn5p7h] FOREIGN KEY([superior_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[users] CHECK CONSTRAINT [FKsh8ndy2iuas3g5iwyr6cn5p7h]
GO
USE [master]
GO
ALTER DATABASE [leave_management] SET  READ_WRITE 
GO
