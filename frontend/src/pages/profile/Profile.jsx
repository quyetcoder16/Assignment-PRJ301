import { useState } from "react";
import {
  Card,
  CardContent,
  Typography,
  Avatar,
  Grid,
  Button,
  TextField,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  List,
  ListItem,
  ListItemText,
} from "@mui/material";
import EditIcon from "@mui/icons-material/Edit";
import { useSelector } from "react-redux";

const Profile = () => {
  const { user } = useSelector((state) => state.authReducer);

  // Kiểm tra dữ liệu user, nếu không có giá trị thì gán default để tránh lỗi
  const defaultUser = {
    userId: "",
    email: "",
    fullName: "",
    phoneNumber: "",
    address: "",
    direct_management: "",
    permissions: [],
  };

  const userData = user || defaultUser;

  const [open, setOpen] = useState(false);
  const [editData, setEditData] = useState(userData);

  const handleOpen = () => {
    setEditData(userData);
    setOpen(true);
  };
  const handleClose = () => setOpen(false);

  const handleSave = () => {
    // TODO: Gửi dữ liệu cập nhật lên server
    console.log("Saved Data:", editData);
    setOpen(false);
  };

  return (
    <div style={{ maxWidth: "600px", margin: "auto", padding: "20px" }}>
      <Typography variant="h5" gutterBottom>
        My Profile
      </Typography>

      {/* Profile Card */}
      <Card sx={{ display: "flex", alignItems: "center", p: 2, mb: 2 }}>
        <Avatar
          src={`https://i.pravatar.cc/150?img=${user?.userId}`}
          sx={{ width: 80, height: 80, mr: 2 }}
        />
        <div>
          <Typography variant="h6">{userData.fullName}</Typography>
          <Typography color="textSecondary">
            {userData.direct_management}
          </Typography>
          <Typography color="textSecondary">{userData.address}</Typography>
        </div>
        <Button
          startIcon={<EditIcon />}
          sx={{ ml: "auto" }}
          onClick={handleOpen}
        >
          Edit
        </Button>
      </Card>

      {/* Personal Info */}
      <Card sx={{ mb: 2 }}>
        <CardContent>
          <Typography variant="h6">Personal Information</Typography>
          <Grid container spacing={2} sx={{ mt: 1 }}>
            <Grid item xs={12}>
              <Typography variant="body2">Full Name</Typography>
              <Typography>{userData.fullName}</Typography>
            </Grid>
            <Grid item xs={12}>
              <Typography variant="body2">Email Address</Typography>
              <Typography>{userData.email}</Typography>
            </Grid>
            <Grid item xs={12}>
              <Typography variant="body2">Phone</Typography>
              <Typography>{userData.phoneNumber}</Typography>
            </Grid>
          </Grid>
        </CardContent>
      </Card>

      {/* Address */}
      <Card sx={{ mb: 2 }}>
        <CardContent>
          <Typography variant="h6">Address</Typography>
          <Typography>{userData.address}</Typography>
        </CardContent>
      </Card>

      {/* Management */}
      <Card sx={{ mb: 2 }}>
        <CardContent>
          <Typography variant="h6">Direct Management</Typography>
          <Typography>{userData.direct_management}</Typography>
        </CardContent>
      </Card>

      {/* Edit Dialog */}
      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>Edit Profile</DialogTitle>
        <DialogContent>
          <Grid container spacing={2}>
            <Grid item xs={12}>
              <TextField
                className="mt-3"
                fullWidth
                label="Full Name"
                value={editData.fullName}
                onChange={(e) =>
                  setEditData({ ...editData, fullName: e.target.value })
                }
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Email"
                value={editData.email}
                onChange={(e) =>
                  setEditData({ ...editData, email: e.target.value })
                }
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Phone"
                value={editData.phoneNumber}
                onChange={(e) =>
                  setEditData({ ...editData, phoneNumber: e.target.value })
                }
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Address"
                value={editData.address}
                onChange={(e) =>
                  setEditData({ ...editData, address: e.target.value })
                }
              />
            </Grid>
          </Grid>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleSave} variant="contained">
            Save
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
};

export default Profile;
