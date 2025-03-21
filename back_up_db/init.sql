USE [leave_management]
GO
/****** Object:  Table [dbo].[department]    Script Date: 3/10/2025 5:22:44 PM ******/
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
/****** Object:  Table [dbo].[employee]    Script Date: 3/10/2025 5:22:44 PM ******/
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
/****** Object:  Table [dbo].[invalidated_token]    Script Date: 3/10/2025 5:22:44 PM ******/
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
/****** Object:  Table [dbo].[otp_tokens]    Script Date: 3/10/2025 5:22:44 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[otp_tokens](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[created_at] [datetime2](6) NOT NULL,
	[email] [varchar](255) NOT NULL,
	[expires_at] [datetime2](6) NOT NULL,
	[otp] [varchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[permission]    Script Date: 3/10/2025 5:22:44 PM ******/
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
/****** Object:  Table [dbo].[request_leave]    Script Date: 3/10/2025 5:22:44 PM ******/
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
/****** Object:  Table [dbo].[request_status]    Script Date: 3/10/2025 5:22:44 PM ******/
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
/****** Object:  Table [dbo].[role]    Script Date: 3/10/2025 5:22:44 PM ******/
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
/****** Object:  Table [dbo].[role_permission]    Script Date: 3/10/2025 5:22:44 PM ******/
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
/****** Object:  Table [dbo].[type_leave]    Script Date: 3/10/2025 5:22:44 PM ******/
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
/****** Object:  Table [dbo].[user_role]    Script Date: 3/10/2025 5:22:44 PM ******/
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
/****** Object:  Table [dbo].[users]    Script Date: 3/10/2025 5:22:44 PM ******/
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
INSERT [dbo].[department] ([dep_id], [manager_id], [dep_name], [description]) VALUES (2, 3, N'QA', N'Phòng ban kiểm thử chất lượng')
INSERT [dbo].[department] ([dep_id], [manager_id], [dep_name], [description]) VALUES (3, NULL, N'Sales', N'Phòng ban bán hàng')
GO
SET IDENTITY_INSERT [dbo].[employee] ON 

INSERT [dbo].[employee] ([dep_id], [emp_id], [manager_id], [full_name], [phone], [address]) VALUES (1, 1, NULL, N'division IT', N'123456789', N'Hà Nội')
INSERT [dbo].[employee] ([dep_id], [emp_id], [manager_id], [full_name], [phone], [address]) VALUES (1, 2, 1, N'leader IT', N'21232342424', N'Hòa Lạc')
INSERT [dbo].[employee] ([dep_id], [emp_id], [manager_id], [full_name], [phone], [address]) VALUES (2, 3, 2, N'Dương Xuân Quyết', N'01234567', N'Thạch Thất')
INSERT [dbo].[employee] ([dep_id], [emp_id], [manager_id], [full_name], [phone], [address]) VALUES (2, 4, 3, N'Bùi Tuấn Hiệp', N'12541643764', N'Vĩnh Phúc')
SET IDENTITY_INSERT [dbo].[employee] OFF
GO
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-09T08:38:23.0000000' AS DateTime2), N'00d4ff97-bc97-44a0-be93-46e3a7208385')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-10T17:10:42.0000000' AS DateTime2), N'00da8862-a46b-4b9c-aa97-517038f8611e')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-10T16:58:56.0000000' AS DateTime2), N'0b2a056d-07e2-43ab-8cd4-8a38d9641498')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-09T08:43:50.0000000' AS DateTime2), N'1036cdad-7236-4a72-91eb-5384cf6277a7')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-10T15:42:33.0000000' AS DateTime2), N'127b8fc4-b963-40cc-a4f2-c2c4c521cb38')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-10T17:11:42.0000000' AS DateTime2), N'1b3c44c9-0c96-4128-be43-19f6aae12d9d')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-10T15:41:39.0000000' AS DateTime2), N'1f35095d-e0f9-4f52-bcb6-94005f685bda')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-09T05:56:48.0000000' AS DateTime2), N'237cef2e-310e-4a38-b87f-2c5bb95b9c3c')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-09T09:40:56.0000000' AS DateTime2), N'29d7cb8a-ebd7-4b0b-a299-ab194d17a52c')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-09T08:37:23.0000000' AS DateTime2), N'2ce1c839-13cb-49e9-86bc-797513f58177')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-10T16:54:52.0000000' AS DateTime2), N'3b7b6e37-3b14-4b3d-aa34-606893c07c82')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-09T09:24:12.0000000' AS DateTime2), N'4378b3ed-065c-497b-b884-887aaf3244be')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-10T15:40:39.0000000' AS DateTime2), N'46eb1875-e06d-4b0a-a6bd-857f26a9c20c')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-09T05:55:48.0000000' AS DateTime2), N'494a0c37-007c-48d4-bd99-065f84bad85d')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-10T16:52:03.0000000' AS DateTime2), N'4bed07de-95e2-4a8a-b99f-50c3a815203c')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-09T09:23:12.0000000' AS DateTime2), N'588a164f-03ca-408f-b09c-0f1246892a1e')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-09T08:44:50.0000000' AS DateTime2), N'5f0bc614-23ad-49bf-a0a5-f627c19edea1')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-10T16:57:56.0000000' AS DateTime2), N'6a43d667-bab9-439f-8589-9a3c8e2b07d1')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-09T08:29:07.0000000' AS DateTime2), N'6fbbda1c-0880-4c29-9196-999da0826a97')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-09T09:31:39.0000000' AS DateTime2), N'76b7956b-00e6-465e-b868-091d68862174')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-10T17:00:00.0000000' AS DateTime2), N'8b2b6f69-c1a8-4578-a931-8e10b6bd52c7')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-10T17:01:00.0000000' AS DateTime2), N'8d22040c-9d95-43c5-b1e4-eab3ba89275f')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-09T09:30:39.0000000' AS DateTime2), N'92467fbb-bafd-4bd6-84e1-178737c444ed')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-09T06:10:15.0000000' AS DateTime2), N'a63d136f-da4c-4858-846e-556a66ab3ad6')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-10T15:41:33.0000000' AS DateTime2), N'bb121c26-9ee4-41b6-b6c5-5f7a17a0cf94')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-09T09:39:56.0000000' AS DateTime2), N'c1dcfc08-7de4-4fb7-ba2f-362d7c254bf0')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-10T16:58:39.0000000' AS DateTime2), N'cc95f41d-3859-420d-9f9f-4997d5d0bd5d')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-10T16:59:39.0000000' AS DateTime2), N'd39b16e9-2243-4813-97d5-1ca9862c1847')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-10T16:53:03.0000000' AS DateTime2), N'f0cc5829-c919-4e75-ad6e-bb0c230fa5e0')
INSERT [dbo].[invalidated_token] ([expiry_time], [id_token]) VALUES (CAST(N'2025-03-10T16:55:52.0000000' AS DateTime2), N'f33e887b-cc6a-4a60-aa9a-e283cb424c39')
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

INSERT [dbo].[request_leave] ([from_date], [id_request], [status_id], [to_date], [type_leave_id], [emp_created], [emp_process], [note_process], [reason], [title], [created_at], [updated_at], [user_created], [user_updated]) VALUES (CAST(N'2025-03-13' AS Date), 1, 3, CAST(N'2025-03-21' AS Date), 1, 2, 1, N'hông bé hơi :)))', N'demo 123', N'leader it leave', CAST(N'2025-03-06T20:27:34.8594800' AS DateTime2), CAST(N'2025-03-07T19:56:47.7513380' AS DateTime2), 2, 1)
INSERT [dbo].[request_leave] ([from_date], [id_request], [status_id], [to_date], [type_leave_id], [emp_created], [emp_process], [note_process], [reason], [title], [created_at], [updated_at], [user_created], [user_updated]) VALUES (CAST(N'2025-03-27' AS Date), 3, 1, CAST(N'2025-03-28' AS Date), 1, 1, NULL, NULL, N'reason 2 oke ngay', N'demo division 2 hihi', CAST(N'2025-03-06T22:52:59.8708800' AS DateTime2), CAST(N'2025-03-07T20:35:31.6103040' AS DateTime2), 1, 1)
INSERT [dbo].[request_leave] ([from_date], [id_request], [status_id], [to_date], [type_leave_id], [emp_created], [emp_process], [note_process], [reason], [title], [created_at], [updated_at], [user_created], [user_updated]) VALUES (CAST(N'2025-04-14' AS Date), 4, 1, CAST(N'2025-04-15' AS Date), 1, 2, NULL, NULL, N'mệt thì nghỉ', N'demo nghỉ 2', CAST(N'2025-03-07T09:12:24.0385500' AS DateTime2), CAST(N'2025-03-07T09:12:24.0385500' AS DateTime2), 2, 2)
INSERT [dbo].[request_leave] ([from_date], [id_request], [status_id], [to_date], [type_leave_id], [emp_created], [emp_process], [note_process], [reason], [title], [created_at], [updated_at], [user_created], [user_updated]) VALUES (CAST(N'2025-04-22' AS Date), 5, 1, CAST(N'2025-05-13' AS Date), 1, 2, NULL, NULL, N'mệt thì nghỉ', N'demo nghỉ 2', CAST(N'2025-03-07T09:12:42.3459840' AS DateTime2), CAST(N'2025-03-07T09:12:42.3459840' AS DateTime2), 2, 2)
INSERT [dbo].[request_leave] ([from_date], [id_request], [status_id], [to_date], [type_leave_id], [emp_created], [emp_process], [note_process], [reason], [title], [created_at], [updated_at], [user_created], [user_updated]) VALUES (CAST(N'2025-03-07' AS Date), 6, 2, CAST(N'2025-03-08' AS Date), 1, 2, 1, N'oke', N'nghỉ helo', N'demo nghỉ 123', CAST(N'2025-03-07T09:13:59.7554890' AS DateTime2), CAST(N'2025-03-07T19:31:52.7587460' AS DateTime2), 2, 1)
INSERT [dbo].[request_leave] ([from_date], [id_request], [status_id], [to_date], [type_leave_id], [emp_created], [emp_process], [note_process], [reason], [title], [created_at], [updated_at], [user_created], [user_updated]) VALUES (CAST(N'2025-07-09' AS Date), 7, 3, CAST(N'2025-07-09' AS Date), 1, 2, 1, N'không được rồi bé', N'kjfshflkaf hế hế ', N'demo_oke', CAST(N'2025-03-07T09:16:23.1157830' AS DateTime2), CAST(N'2025-03-07T19:30:36.9998840' AS DateTime2), 2, 1)
INSERT [dbo].[request_leave] ([from_date], [id_request], [status_id], [to_date], [type_leave_id], [emp_created], [emp_process], [note_process], [reason], [title], [created_at], [updated_at], [user_created], [user_updated]) VALUES (CAST(N'2025-06-04' AS Date), 8, 1, CAST(N'2025-06-04' AS Date), 2, 2, NULL, NULL, N'hế hế ', N'oke nhé', CAST(N'2025-03-07T09:17:04.2799320' AS DateTime2), CAST(N'2025-03-07T09:17:04.2799320' AS DateTime2), 2, 2)
INSERT [dbo].[request_leave] ([from_date], [id_request], [status_id], [to_date], [type_leave_id], [emp_created], [emp_process], [note_process], [reason], [title], [created_at], [updated_at], [user_created], [user_updated]) VALUES (CAST(N'2025-05-16' AS Date), 150, 3, CAST(N'2025-05-16' AS Date), 1, 2, 1, N'không không', N'test 123', N'demo 123', CAST(N'2025-03-07T09:52:52.9850840' AS DateTime2), CAST(N'2025-03-07T19:31:34.4680290' AS DateTime2), 2, 1)
INSERT [dbo].[request_leave] ([from_date], [id_request], [status_id], [to_date], [type_leave_id], [emp_created], [emp_process], [note_process], [reason], [title], [created_at], [updated_at], [user_created], [user_updated]) VALUES (CAST(N'2025-07-25' AS Date), 151, 3, CAST(N'2025-07-25' AS Date), 1, 2, 1, N'không bé ơi', N'12345', N'done oke la', CAST(N'2025-03-07T10:06:40.7618290' AS DateTime2), CAST(N'2025-03-07T19:30:10.0169870' AS DateTime2), 2, 1)
INSERT [dbo].[request_leave] ([from_date], [id_request], [status_id], [to_date], [type_leave_id], [emp_created], [emp_process], [note_process], [reason], [title], [created_at], [updated_at], [user_created], [user_updated]) VALUES (CAST(N'2025-08-21' AS Date), 152, 3, CAST(N'2025-08-21' AS Date), 2, 2, 1, N'không bé ơi', N'jkHLKSAH FKA', N'demo oke jhkjdshfa al', CAST(N'2025-03-07T10:08:30.5470640' AS DateTime2), CAST(N'2025-03-07T20:20:10.1290720' AS DateTime2), 2, 1)
INSERT [dbo].[request_leave] ([from_date], [id_request], [status_id], [to_date], [type_leave_id], [emp_created], [emp_process], [note_process], [reason], [title], [created_at], [updated_at], [user_created], [user_updated]) VALUES (CAST(N'2025-09-11' AS Date), 153, 2, CAST(N'2025-09-11' AS Date), 1, 2, 1, N'Hehe', N'klsajlfdjsa', N'ksljdlksajl', CAST(N'2025-03-07T10:12:02.4880200' AS DateTime2), CAST(N'2025-03-07T19:29:41.4926510' AS DateTime2), 2, 1)
INSERT [dbo].[request_leave] ([from_date], [id_request], [status_id], [to_date], [type_leave_id], [emp_created], [emp_process], [note_process], [reason], [title], [created_at], [updated_at], [user_created], [user_updated]) VALUES (CAST(N'2025-02-07' AS Date), 154, 3, CAST(N'2025-09-03' AS Date), 2, 2, 1, N'oke nhé e', N'fake reason', N'fake', CAST(N'2025-03-07T16:19:47.3042250' AS DateTime2), CAST(N'2025-03-07T19:14:56.7060750' AS DateTime2), 2, 1)
INSERT [dbo].[request_leave] ([from_date], [id_request], [status_id], [to_date], [type_leave_id], [emp_created], [emp_process], [note_process], [reason], [title], [created_at], [updated_at], [user_created], [user_updated]) VALUES (CAST(N'2025-09-18' AS Date), 158, 3, CAST(N'2025-09-18' AS Date), 2, 2, 1, N'ko bé ơi
', N'fake reason', N'xin nghỉ fake', CAST(N'2025-03-08T07:29:34.5643520' AS DateTime2), CAST(N'2025-03-08T15:38:23.4126000' AS DateTime2), 2, 1)
INSERT [dbo].[request_leave] ([from_date], [id_request], [status_id], [to_date], [type_leave_id], [emp_created], [emp_process], [note_process], [reason], [title], [created_at], [updated_at], [user_created], [user_updated]) VALUES (CAST(N'2025-03-10' AS Date), 159, 3, CAST(N'2025-03-12' AS Date), 2, 3, 1, N'không được e ơi', N'nay e bị ốm e xin nghỉ', N'Xin Nghỉ ốm', CAST(N'2025-03-09T05:47:30.4594470' AS DateTime2), CAST(N'2025-03-09T08:12:35.2188790' AS DateTime2), 3, 1)
INSERT [dbo].[request_leave] ([from_date], [id_request], [status_id], [to_date], [type_leave_id], [emp_created], [emp_process], [note_process], [reason], [title], [created_at], [updated_at], [user_created], [user_updated]) VALUES (CAST(N'2025-03-27' AS Date), 160, 2, CAST(N'2025-03-29' AS Date), 1, 3, 2, N'oke e nghỉ đi', N'sắp tới e có việc bận cần giải quyết', N'Xin Nghỉ phép có lương', CAST(N'2025-03-09T08:18:45.5554920' AS DateTime2), CAST(N'2025-03-09T08:19:19.9423330' AS DateTime2), 3, 2)
INSERT [dbo].[request_leave] ([from_date], [id_request], [status_id], [to_date], [type_leave_id], [emp_created], [emp_process], [note_process], [reason], [title], [created_at], [updated_at], [user_created], [user_updated]) VALUES (CAST(N'2025-04-20' AS Date), 161, 2, CAST(N'2025-04-22' AS Date), 2, 3, 1, N'em nghỉ đi', N'em ốm quá', N'demo nghỉ ốm 1', CAST(N'2025-03-10T15:32:57.9194840' AS DateTime2), CAST(N'2025-03-10T15:33:25.7472870' AS DateTime2), 3, 1)
INSERT [dbo].[request_leave] ([from_date], [id_request], [status_id], [to_date], [type_leave_id], [emp_created], [emp_process], [note_process], [reason], [title], [created_at], [updated_at], [user_created], [user_updated]) VALUES (CAST(N'2025-03-10' AS Date), 162, 2, CAST(N'2025-03-11' AS Date), 2, 4, 3, N'oke e nghỉ đi', N'hiệp mệt quá', N'Hiệp Xin Nghỉ', CAST(N'2025-03-10T16:48:36.3956570' AS DateTime2), CAST(N'2025-03-10T16:51:36.8903250' AS DateTime2), 5, 3)
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
INSERT [dbo].[user_role] ([user_id], [role_name]) VALUES (3, N'DIVISION_LEADER')
INSERT [dbo].[user_role] ([user_id], [role_name]) VALUES (5, N'EMPLOYEE')
GO
SET IDENTITY_INSERT [dbo].[users] ON 

INSERT [dbo].[users] ([emp_id], [user_id], [email], [password]) VALUES (1, 1, N'divisionit@prj.com', N'$2y$10$BbX72uUh2SD9ZqsqaW5oG.XygalBQdiwnfcM53TcXogX36yHvjOY2')
INSERT [dbo].[users] ([emp_id], [user_id], [email], [password]) VALUES (2, 2, N'leaderIT1@prj.com', N'$2y$10$BbX72uUh2SD9ZqsqaW5oG.XygalBQdiwnfcM53TcXogX36yHvjOY2')
INSERT [dbo].[users] ([emp_id], [user_id], [email], [password]) VALUES (3, 3, N'quyetdx16@gmail.com', N'$2a$10$25vQVhVrNXtQnIHTy1LoJeiBEVZK7O790kBbxhlmztkQY5SqZ0C5e')
INSERT [dbo].[users] ([emp_id], [user_id], [email], [password]) VALUES (4, 5, N'buituanhiepvinhtuong@gmail.com', N'$2a$10$9YGzdLaMnvhFT/sACOepW.75fReN8UKYBgkso4FfiTnlCzoRVKWK2')
SET IDENTITY_INSERT [dbo].[users] OFF
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UKgy16pd6s8as0hxesl21n5e6l2]    Script Date: 3/10/2025 5:22:44 PM ******/
ALTER TABLE [dbo].[otp_tokens] ADD  CONSTRAINT [UKgy16pd6s8as0hxesl21n5e6l2] UNIQUE NONCLUSTERED 
(
	[email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UK6dotkott2kjsp8vw4d0m25fb7]    Script Date: 3/10/2025 5:22:44 PM ******/
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
