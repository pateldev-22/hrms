import { RouterProvider, createBrowserRouter } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { Toaster } from 'react-hot-toast';

import DashboardLayout from './components/layout/DashboardLayout';
import ProtectedRoute from './components/layout/ProtectedRoute';

import Login from './pages/auth/Login';
import Signup from './pages/auth/Signup';

import Dashboard from './pages/dashboard/Dashboard';
import Travels from './pages/dashboard/Travels';


const router = createBrowserRouter([
  {
    path: '/login',
    element: <Login />,
  },
  {
    path: '/signup',
    element: <Signup />,
  },
  {
    path: '/',
    element: (
        <DashboardLayout />
    ),
    children: [
      {
        index: true,
        element: <Dashboard />,
      },
      {
        path: 'travels',
        element: <Travels />,
      }
      // {
      //   path: 'expenses',
      //   element: <Expenses />,
      // },
      // {
      //   path: 'achievements',
      //   element: <Achievements />,
      // },
      // {
      //   path: 'games',
      //   element: <Games />,
      // },
      // {
      //   path: 'org-chart',
      //   element: <OrgChart />,
      // },
      // {
      //   path: 'jobs',
      //   element: <Jobs />,
      // },
    ],
  },
]);

function App() {
  return (
    <AuthProvider>
      <RouterProvider router={router} />
      <Toaster
        position="top-right"
        toastOptions={{
          duration: 3000,
          style: {
            background: '#363636',
            color: '#fff',
          },
          success: {
            iconTheme: {
              primary: '#22c55e',
              secondary: '#fff',
            },
          },
          error: {
            iconTheme: {
              primary: '#ef4444',
              secondary: '#fff',
            },
          },
        }}
      />
    </AuthProvider>
  );
}

export default App;