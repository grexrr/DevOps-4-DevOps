import {
    Box,
    Card,
    CardContent,
    CircularProgress,
    Grid,
    Typography
} from '@mui/material';
import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { CartesianGrid, Line, LineChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts';

const Dashboard = () => {
  const [metrics, setMetrics] = useState(null);
  const [apps, setApps] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        // Fetch metrics and apps data
        const [metricsResponse, appsResponse] = await Promise.all([
          axios.get('/api/metrics'),
          axios.get('/api/apps')
        ]);
        
        setMetrics(metricsResponse.data);
        setApps(appsResponse.data);
        setLoading(false);
      } catch (error) {
        console.error('Error fetching data:', error);
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  // Generate fake chart data
  const chartData = [
    { name: 'Mon', builds: 4 },
    { name: 'Tue', builds: 3 },
    { name: 'Wed', builds: 6 },
    { name: 'Thu', builds: 4 },
    { name: 'Fri', builds: 5 },
    { name: 'Sat', builds: 2 },
    { name: 'Sun', builds: 1 }
  ];

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" height="400px">
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box sx={{ flexGrow: 1, p: 3 }}>
      <Typography variant="h4" gutterBottom>
        DevOps Console Dashboard
      </Typography>
      
      <Grid container spacing={3}>
        {/* Build Trends Chart */}
        <Grid item xs={12} md={8}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                Build Trends (Last 7 Days)
              </Typography>
              <ResponsiveContainer width="100%" height={300}>
                <LineChart data={chartData}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="name" />
                  <YAxis />
                  <Tooltip />
                  <Line type="monotone" dataKey="builds" stroke="#8884d8" strokeWidth={2} />
                </LineChart>
              </ResponsiveContainer>
            </CardContent>
          </Card>
        </Grid>

        {/* Metrics Cards */}
        <Grid item xs={12} md={4}>
          <Grid container spacing={2}>
            <Grid item xs={12}>
              <Card>
                <CardContent>
                  <Typography color="textSecondary" gutterBottom>
                    Total Builds
                  </Typography>
                  <Typography variant="h4">
                    {metrics?.totalBuilds || 0}
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
            
            <Grid item xs={12}>
              <Card>
                <CardContent>
                  <Typography color="textSecondary" gutterBottom>
                    Success Rate
                  </Typography>
                  <Typography variant="h4">
                    {metrics ? Math.round((metrics.successfulBuilds / metrics.totalBuilds) * 100) : 0}%
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
          </Grid>
        </Grid>
      </Grid>
    </Box>
  );
};

export default Dashboard;