USE [leave_management]
GO
/****** Object:  Table [dbo].[department]    Script Date: 3/7/2025 10:14:45 AM ******/
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
/****** Object:  Table [dbo].[employee]    Script Date: 3/7/2025 10:14:45 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[employee](
	[dep_id] [int] NULL,
	[emp_id] [bigint] IDENTITY(1,1) NOT NULL,
	[manager_id] [bigint] NULL,
	[full_name] [nvarchar](255) NOT NULL,
	[phone] [varchar](255) NULL,
	[address] [nvarchar](255) NULL,
 CONSTRAINT [PK__employee__1299A8618C97DAF3] PRIMARY KEY CLUSTERED 
(
	[emp_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[invalidated_token]    Script Date: 3/7/2025 10:14:45 AM ******/
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
/****** Object:  Table [dbo].[permission]    Script Date: 3/7/2025 10:14:45 AM ******/
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
/****** Object:  Table [dbo].[request_leave]    Script Date: 3/7/2025 10:14:45 AM ******/
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
	[emp_created] [bigint] NOT NULL,
	[emp_process] [bigint] NULL,
	[note_process] [nvarchar](1000) NULL,
	[reason] [nvarchar](1000) NULL,
	[title] [nvarchar](255) NULL,
	[created_at] [datetime2](6) NULL,
	[updated_at] [datetime2](6) NULL,
	[user_created] [bigint] NULL,
	[user_updated] [bigint] NULL,
PRIMARY KEY CLUSTERED 
(
	[id_request] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[request_status]    Script Date: 3/7/2025 10:14:45 AM ******/
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
/****** Object:  Table [dbo].[role]    Script Date: 3/7/2025 10:14:45 AM ******/
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
/****** Object:  Table [dbo].[role_permission]    Script Date: 3/7/2025 10:14:45 AM ******/
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
/****** Object:  Table [dbo].[type_leave]    Script Date: 3/7/2025 10:14:45 AM ******/
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
/****** Object:  Table [dbo].[user_role]    Script Date: 3/7/2025 10:14:45 AM ******/
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
/****** Object:  Table [dbo].[users]    Script Date: 3/7/2025 10:14:45 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[users](
	[emp_id] [bigint] NULL,
	[user_id] [bigint] IDENTITY(1,1) NOT NULL,
	[email] [varchar](255) NOT NULL,
	[password] [varchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[user_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
INSERT [dbo].[department] ([dep_id], [manager_id], [dep_name], [description]) VALUES (1, 1, N'IT', N'Phòng ban công nghệ thông tin')
INSERT [dbo].[department] ([dep_id], [manager_id], [dep_name], [description]) VALUES (2, NULL, N'QA', N'Phòng ban kiểm thử chất lượng')
INSERT [dbo].[department] ([dep_id], [manager_id], [dep_name], [description]) VALUES (3, NULL, N'Sales', N'Phòng ban bán hàng')
GO
SET IDENTITY_INSERT [dbo].[employee] ON 

INSERT [dbo].[employee] ([dep_id], [emp_id], [manager_id], [full_name], [phone], [address]) VALUES (1, 1, NULL, N'division IT', N'123456789', N'Hà Nội')
INSERT [dbo].[employee] ([dep_id], [emp_id], [manager_id], [full_name], [phone], [address]) VALUES (1, 2, 1, N'leader IT', N'21232342424', N'Hòa Lạc')
SET IDENTITY_INSERT [dbo].[employee] OFF
GO
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-06T19:42:56.0000000' AS DateTime2), N'0e507783-73ac-476a-9e9d-130e2929fc2e')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-07T08:46:17.0000000' AS DateTime2), N'0f4c9ee5-f891-4d42-bf85-a7caa03822d9')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-07T09:09:17.0000000' AS DateTime2), N'2909dcc4-618c-4cb1-b12c-f3d265e6d5b7')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-06T19:14:25.0000000' AS DateTime2), N'3e668f9f-8f96-468e-bb8c-3aecbf120bef')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-06T19:13:39.0000000' AS DateTime2), N'512e19f4-3a22-40cd-9438-647ffe21cb31')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-07T08:26:24.0000000' AS DateTime2), N'7abb5795-cbf1-4035-906e-38b0ce891b12')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-06T19:15:25.0000000' AS DateTime2), N'7d995290-c428-4866-826c-0745f2528812')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-06T19:14:39.0000000' AS DateTime2), N'81a2cfbf-b135-47a2-bb51-5803b4ac81e7')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-06T19:41:14.0000000' AS DateTime2), N'8c2731a9-6cfc-46d7-842d-e5674aa92791')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-06T19:43:56.0000000' AS DateTime2), N'93a2ae87-5e5d-439d-a128-dd7fcf8ab04b')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-07T10:12:26.0000000' AS DateTime2), N'a8d20c9d-efed-4708-9900-73fe00546e43')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-07T09:10:17.0000000' AS DateTime2), N'aad31bb4-d5e7-4143-8257-798051cd7868')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-06T19:42:14.0000000' AS DateTime2), N'aba01dfb-9ed4-4cae-a262-bd8785262bb4')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-06T19:43:14.0000000' AS DateTime2), N'd438759d-25f7-463c-b564-cfdc136d8f57')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-07T09:19:04.0000000' AS DateTime2), N'dac1f67b-3d00-4417-af7a-f9e1e9fc27e5')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-06T19:44:14.0000000' AS DateTime2), N'e15c7f0e-88f5-4f05-88b1-54f8f1847ca9')
GO
INSERT [dbo].[permission] ([description], [permission_name]) VALUES (N'Approving applications from subordinates', N'APPOVAL_REQUEST')
INSERT [dbo].[permission] ([description], [permission_name]) VALUES (N'create my request', N'CREATED_MY_REQUEST')
INSERT [dbo].[permission] ([description], [permission_name]) VALUES (N'delete my request', N'DELETE_MY_REQUEST')
INSERT [dbo].[permission] ([description], [permission_name]) VALUES (N'edit my request', N'EDIT_MY_REQUEST')
INSERT [dbo].[permission] ([description], [permission_name]) VALUES (N'view agenda of employee', N'VIEW_AGENDA')
INSERT [dbo].[permission] ([description], [permission_name]) VALUES (N'view my request', N'VIEW_MY_REQUEST')
INSERT [dbo].[permission] ([description], [permission_name]) VALUES (N'view subordinate''s application', N'VIEW_SUB_REQUEST')
GO
SET IDENTITY_INSERT [dbo].[request_leave] ON 

INSERT [dbo].[request_leave] ([from_date], [id_request], [status_id], [to_date], [type_leave_id], [emp_created], [emp_process], [note_process], [reason], [title], [created_at], [updated_at], [user_created], [user_updated]) VALUES (CAST(N'2025-03-13' AS Date), 1, 1, CAST(N'2025-03-21' AS Date), 1, 2, NULL, NULL, N'demo 123', N'leader it leave', CAST(N'2025-03-06T20:27:34.8594800' AS DateTime2), CAST(N'2025-03-06T20:27:34.8604750' AS DateTime2), 2, 2)
INSERT [dbo].[request_leave] ([from_date], [id_request], [status_id], [to_date], [type_leave_id], [emp_created], [emp_process], [note_process], [reason], [title], [created_at], [updated_at], [user_created], [user_updated]) VALUES (CAST(N'2025-03-06' AS Date), 2, 1, CAST(N'2025-03-14' AS Date), 1, 1, NULL, NULL, N'demo reson', N'division IT', CAST(N'2025-03-06T22:51:09.7788440' AS DateTime2), CAST(N'2025-03-06T22:51:09.7788440' AS DateTime2), 1, 1)
INSERT [dbo].[request_leave] ([from_date], [id_request], [status_id], [to_date], [type_leave_id], [emp_created], [emp_process], [note_process], [reason], [title], [created_at], [updated_at], [user_created], [user_updated]) VALUES (CAST(N'2025-03-27' AS Date), 3, 1, CAST(N'2025-03-28' AS Date), 2, 1, NULL, NULL, N'reason 2', N'demo division 2', CAST(N'2025-03-06T22:52:59.8708800' AS DateTime2), CAST(N'2025-03-06T22:52:59.8708800' AS DateTime2), 1, 1)
INSERT [dbo].[request_leave] ([from_date], [id_request], [status_id], [to_date], [type_leave_id], [emp_created], [emp_process], [note_process], [reason], [title], [created_at], [updated_at], [user_created], [user_updated]) VALUES (CAST(N'2025-04-14' AS Date), 4, 1, CAST(N'2025-04-15' AS Date), 1, 2, NULL, NULL, N'mệt thì nghỉ', N'demo nghỉ 2', CAST(N'2025-03-07T09:12:24.0385500' AS DateTime2), CAST(N'2025-03-07T09:12:24.0385500' AS DateTime2), 2, 2)
INSERT [dbo].[request_leave] ([from_date], [id_request], [status_id], [to_date], [type_leave_id], [emp_created], [emp_process], [note_process], [reason], [title], [created_at], [updated_at], [user_created], [user_updated]) VALUES (CAST(N'2025-04-22' AS Date), 5, 1, CAST(N'2025-05-13' AS Date), 1, 2, NULL, NULL, N'mệt thì nghỉ', N'demo nghỉ 2', CAST(N'2025-03-07T09:12:42.3459840' AS DateTime2), CAST(N'2025-03-07T09:12:42.3459840' AS DateTime2), 2, 2)
INSERT [dbo].[request_leave] ([from_date], [id_request], [status_id], [to_date], [type_leave_id], [emp_created], [emp_process], [note_process], [reason], [title], [created_at], [updated_at], [user_created], [user_updated]) VALUES (CAST(N'2025-03-07' AS Date), 6, 1, CAST(N'2025-03-08' AS Date), 1, 2, NULL, NULL, N'nghỉ helo', N'demo nghỉ 123', CAST(N'2025-03-07T09:13:59.7554890' AS DateTime2), CAST(N'2025-03-07T09:13:59.7554890' AS DateTime2), 2, 2)
INSERT [dbo].[request_leave] ([from_date], [id_request], [status_id], [to_date], [type_leave_id], [emp_created], [emp_process], [note_process], [reason], [title], [created_at], [updated_at], [user_created], [user_updated]) VALUES (CAST(N'2025-07-09' AS Date), 7, 1, CAST(N'2025-07-09' AS Date), 1, 2, NULL, NULL, N'kjfshflkaf hế hế ', N'demo_oke', CAST(N'2025-03-07T09:16:23.1157830' AS DateTime2), CAST(N'2025-03-07T09:16:23.1157830' AS DateTime2), 2, 2)
INSERT [dbo].[request_leave] ([from_date], [id_request], [status_id], [to_date], [type_leave_id], [emp_created], [emp_process], [note_process], [reason], [title], [created_at], [updated_at], [user_created], [user_updated]) VALUES (CAST(N'2025-06-04' AS Date), 8, 1, CAST(N'2025-06-04' AS Date), 2, 2, NULL, NULL, N'hế hế ', N'oke nhé', CAST(N'2025-03-07T09:17:04.2799320' AS DateTime2), CAST(N'2025-03-07T09:17:04.2799320' AS DateTime2), 2, 2)
INSERT [dbo].[request_leave] ([from_date], [id_request], [status_id], [to_date], [type_leave_id], [emp_created], [emp_process], [note_process], [reason], [title], [created_at], [updated_at], [user_created], [user_updated]) VALUES (CAST(N'2025-05-16' AS Date), 150, 2, CAST(N'2025-05-16' AS Date), 1, 2, NULL, NULL, N'test 123', N'demo 123', CAST(N'2025-03-07T09:52:52.9850840' AS DateTime2), CAST(N'2025-03-07T09:52:52.9850840' AS DateTime2), 2, 2)
INSERT [dbo].[request_leave] ([from_date], [id_request], [status_id], [to_date], [type_leave_id], [emp_created], [emp_process], [note_process], [reason], [title], [created_at], [updated_at], [user_created], [user_updated]) VALUES (CAST(N'2025-07-25' AS Date), 151, 1, CAST(N'2025-07-25' AS Date), 1, 2, NULL, NULL, N'12345', N'done oke la', CAST(N'2025-03-07T10:06:40.7618290' AS DateTime2), CAST(N'2025-03-07T10:06:40.7618290' AS DateTime2), 2, 2)
INSERT [dbo].[request_leave] ([from_date], [id_request], [status_id], [to_date], [type_leave_id], [emp_created], [emp_process], [note_process], [reason], [title], [created_at], [updated_at], [user_created], [user_updated]) VALUES (CAST(N'2025-08-21' AS Date), 152, 1, CAST(N'2025-08-21' AS Date), 2, 2, NULL, NULL, N'jkHLKSAH FKA', N'demo oke jhkjdshfa al', CAST(N'2025-03-07T10:08:30.5470640' AS DateTime2), CAST(N'2025-03-07T10:08:30.5470640' AS DateTime2), 2, 2)
INSERT [dbo].[request_leave] ([from_date], [id_request], [status_id], [to_date], [type_leave_id], [emp_created], [emp_process], [note_process], [reason], [title], [created_at], [updated_at], [user_created], [user_updated]) VALUES (CAST(N'2025-09-11' AS Date), 153, 1, CAST(N'2025-09-11' AS Date), 1, 2, NULL, NULL, N'klsajlfdjsa', N'ksljdlksajl', CAST(N'2025-03-07T10:12:02.4880200' AS DateTime2), CAST(N'2025-03-07T10:12:02.4880200' AS DateTime2), 2, 2)
SET IDENTITY_INSERT [dbo].[request_leave] OFF
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
INSERT [dbo].[type_leave] ([type_leave_id], [description], [name_type_leave]) VALUES (3, N'Maternity leave', N'Maternity leave')
SET IDENTITY_INSERT [dbo].[type_leave] OFF
GO
INSERT [dbo].[user_role] ([user_id], [role_name]) VALUES (1, N'DIVISION_LEADER')
INSERT [dbo].[user_role] ([user_id], [role_name]) VALUES (2, N'LEADER')
GO
SET IDENTITY_INSERT [dbo].[users] ON 

INSERT [dbo].[users] ([emp_id], [user_id], [email], [password]) VALUES (1, 1, N'divisionit@prj.com', N'$2y$10$BbX72uUh2SD9ZqsqaW5oG.XygalBQdiwnfcM53TcXogX36yHvjOY2')
INSERT [dbo].[users] ([emp_id], [user_id], [email], [password]) VALUES (2, 2, N'leaderIT1@prj.com', N'$2y$10$BbX72uUh2SD9ZqsqaW5oG.XygalBQdiwnfcM53TcXogX36yHvjOY2')
SET IDENTITY_INSERT [dbo].[users] OFF
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UK6dotkott2kjsp8vw4d0m25fb7]    Script Date: 3/7/2025 10:14:45 AM ******/
ALTER TABLE [dbo].[users] ADD  CONSTRAINT [UK6dotkott2kjsp8vw4d0m25fb7] UNIQUE NONCLUSTERED 
(
	[email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
ALTER TABLE [dbo].[department]  WITH CHECK ADD  CONSTRAINT [FKmp68t6xcos91gmiaj59fi077o] FOREIGN KEY([manager_id])
REFERENCES [dbo].[employee] ([emp_id])
GO
ALTER TABLE [dbo].[department] CHECK CONSTRAINT [FKmp68t6xcos91gmiaj59fi077o]
GO
ALTER TABLE [dbo].[employee]  WITH CHECK ADD  CONSTRAINT [FKe18129hcd0klevt866ww22wnd] FOREIGN KEY([dep_id])
REFERENCES [dbo].[department] ([dep_id])
GO
ALTER TABLE [dbo].[employee] CHECK CONSTRAINT [FKe18129hcd0klevt866ww22wnd]
GO
ALTER TABLE [dbo].[employee]  WITH CHECK ADD  CONSTRAINT [FKou6wbxug1d0qf9mabut3xqblo] FOREIGN KEY([manager_id])
REFERENCES [dbo].[employee] ([emp_id])
GO
ALTER TABLE [dbo].[employee] CHECK CONSTRAINT [FKou6wbxug1d0qf9mabut3xqblo]
GO
ALTER TABLE [dbo].[request_leave]  WITH CHECK ADD  CONSTRAINT [FKcpivoscgfi401d8bne0urvr8k] FOREIGN KEY([emp_process])
REFERENCES [dbo].[employee] ([emp_id])
GO
ALTER TABLE [dbo].[request_leave] CHECK CONSTRAINT [FKcpivoscgfi401d8bne0urvr8k]
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
ALTER TABLE [dbo].[request_leave]  WITH CHECK ADD  CONSTRAINT [FKsgkm16fnu97cvk7ehq32yp7jc] FOREIGN KEY([emp_created])
REFERENCES [dbo].[employee] ([emp_id])
GO
ALTER TABLE [dbo].[request_leave] CHECK CONSTRAINT [FKsgkm16fnu97cvk7ehq32yp7jc]
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
ALTER TABLE [dbo].[users]  WITH CHECK ADD  CONSTRAINT [FKgxe7d26tglmhy6itjqxv217t] FOREIGN KEY([emp_id])
REFERENCES [dbo].[employee] ([emp_id])
GO
ALTER TABLE [dbo].[users] CHECK CONSTRAINT [FKgxe7d26tglmhy6itjqxv217t]
GO
