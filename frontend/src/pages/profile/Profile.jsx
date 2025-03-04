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
} from "@mui/material";
import EditIcon from "@mui/icons-material/Edit";

const Profile = () => {
  const [open, setOpen] = useState(false);
  const [user, setUser] = useState({
    avatar: "https://i.pravatar.cc/150?img=3",
    firstName: "Rafiquar",
    lastName: "Rahman",
    email: "rafiqurrahman51@gmail.com",
    phone: "+09 345 346 46",
    bio: "Team Manager",
    country: "United Kingdom",
    city: "Leeds, East London",
  });

  const [editData, setEditData] = useState(user);

  const handleOpen = () => {
    setEditData(user);
    setOpen(true);
  };
  const handleClose = () => setOpen(false);

  const handleSave = () => {
    setUser(editData);
    setOpen(false);
  };

  return (
    <div style={{ maxWidth: "600px", margin: "auto", padding: "20px" }}>
      <Typography variant="h5" gutterBottom>
        My Profile
      </Typography>

      {/* Profile Card */}
      <Card sx={{ display: "flex", alignItems: "center", p: 2, mb: 2 }}>
        <Avatar src={user.avatar} sx={{ width: 80, height: 80, mr: 2 }} />
        <div>
          <Typography variant="h6">{`${user.firstName} ${user.lastName}`}</Typography>
          <Typography color="textSecondary">{user.bio}</Typography>
          <Typography color="textSecondary">
            {user.city}, {user.country}
          </Typography>
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
            <Grid item xs={6}>
              <Typography variant="body2">First Name</Typography>
              <Typography>{user.firstName}</Typography>
            </Grid>
            <Grid item xs={6}>
              <Typography variant="body2">Last Name</Typography>
              <Typography>{user.lastName}</Typography>
            </Grid>
            <Grid item xs={6}>
              <Typography variant="body2">Email Address</Typography>
              <Typography>{user.email}</Typography>
            </Grid>
            <Grid item xs={6}>
              <Typography variant="body2">Phone</Typography>
              <Typography>{user.phone}</Typography>
            </Grid>
            <Grid item xs={12}>
              <Typography variant="body2">Bio</Typography>
              <Typography>{user.bio}</Typography>
            </Grid>
          </Grid>
        </CardContent>
      </Card>

      {/* Address */}
      <Card>
        <CardContent>
          <Typography variant="h6">Address</Typography>
          <Grid container spacing={2} sx={{ mt: 1 }}>
            <Grid item xs={6}>
              <Typography variant="body2">Country</Typography>
              <Typography>{user.country}</Typography>
            </Grid>
            <Grid item xs={6}>
              <Typography variant="body2">City/State</Typography>
              <Typography>{user.city}</Typography>
            </Grid>
          </Grid>
        </CardContent>
      </Card>

      {/* Edit Dialog */}
      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>Edit Profile</DialogTitle>
        <DialogContent>
          <Grid container spacing={2}>
            <Grid item xs={6}>
              <TextField
                fullWidth
                label="First Name"
                value={editData.firstName}
                onChange={(e) =>
                  setEditData({ ...editData, firstName: e.target.value })
                }
              />
            </Grid>
            <Grid item xs={6}>
              <TextField
                fullWidth
                label="Last Name"
                value={editData.lastName}
                onChange={(e) =>
                  setEditData({ ...editData, lastName: e.target.value })
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
                value={editData.phone}
                onChange={(e) =>
                  setEditData({ ...editData, phone: e.target.value })
                }
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Bio"
                value={editData.bio}
                onChange={(e) =>
                  setEditData({ ...editData, bio: e.target.value })
                }
              />
            </Grid>
            <Grid item xs={6}>
              <TextField
                fullWidth
                label="Country"
                value={editData.country}
                onChange={(e) =>
                  setEditData({ ...editData, country: e.target.value })
                }
              />
            </Grid>
            <Grid item xs={6}>
              <TextField
                fullWidth
                label="City/State"
                value={editData.city}
                onChange={(e) =>
                  setEditData({ ...editData, city: e.target.value })
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
