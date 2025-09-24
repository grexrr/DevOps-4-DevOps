import {
    Box,
    Button,
    Card,
    CardContent,
    CircularProgress,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    Grid,
    TextField,
    Typography
} from '@mui/material';
import axios from 'axios';
import React, { useEffect, useState } from 'react';

const Apps = () => {
  const [apps, setApps] = useState([]);
  const [loading, setLoading] = useState(true);
  const [open, setOpen] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    gitRepo: '',
    branch: 'main',
    templateId: 'java-maven-template'
  });

  useEffect(() => {
    fetchApps();
  }, []);

  const fetchApps = async () => {
    try {
      const response = await axios.get('/api/apps');
      setApps(response.data);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching apps:', error);
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post('/api/register-apps', formData);
      setOpen(false);
      setFormData({ name: '', gitRepo: '', branch: 'main', templateId: 'java-maven-template' });
      fetchApps(); // Refresh the list
    } catch (error) {
      console.error('Error registering app:', error);
    }
  };

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" height="400px">
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box sx={{ flexGrow: 1, p: 3 }}>
      <Box display="flex" justifyContent="space-between" alignItems="center" mb={3}>
        <Typography variant="h4">
          Applications
        </Typography>
        <Button variant="contained" onClick={() => setOpen(true)}>
          Register New App
        </Button>
      </Box>

      <Grid container spacing={3}>
        {apps.map((app) => (
          <Grid item xs={12} md={6} lg={4} key={app.id}>
            <Card>
              <CardContent>
                <Typography variant="h6" gutterBottom>
                  {app.name}
                </Typography>
                <Typography color="textSecondary" gutterBottom>
                  Repository: {app.repo}
                </Typography>
                <Typography color="textSecondary">
                  ID: {app.id}
                </Typography>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      {/* Registration Dialog */}
      <Dialog open={open} onClose={() => setOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>Register New Application</DialogTitle>
        <form onSubmit={handleSubmit}>
          <DialogContent>
            <TextField
              autoFocus
              margin="dense"
              label="Application Name"
              fullWidth
              variant="outlined"
              value={formData.name}
              onChange={(e) => setFormData({...formData, name: e.target.value})}
              required
            />
            <TextField
              margin="dense"
              label="Git Repository URL"
              fullWidth
              variant="outlined"
              value={formData.gitRepo}
              onChange={(e) => setFormData({...formData, gitRepo: e.target.value})}
              required
            />
            <TextField
              margin="dense"
              label="Branch"
              fullWidth
              variant="outlined"
              value={formData.branch}
              onChange={(e) => setFormData({...formData, branch: e.target.value})}
            />
            <TextField
              margin="dense"
              label="Template ID"
              fullWidth
              variant="outlined"
              value={formData.templateId}
              onChange={(e) => setFormData({...formData, templateId: e.target.value})}
            />
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setOpen(false)}>Cancel</Button>
            <Button type="submit" variant="contained">Register</Button>
          </DialogActions>
        </form>
      </Dialog>
    </Box>
  );
};

export default Apps;