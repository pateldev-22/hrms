import { RouterProvider, createBrowserRouter } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { Toaster } from 'react-hot-toast';

import DashboardLayout from './components/layout/DashboardLayout';
import ProtectedRoute from './components/layout/ProtectedRoute';

import Login from './pages/auth/Login';
import Signup from './pages/auth/Signup';

import Dashboard from './pages/dashboard/Dashboard';
import Travels from './pages/dashboard/Travels';
import TravelDetails from './pages/dashboard/TravelDetails';
import NotificationsPage from './pages/dashboard/NotificationsPage';
import Expense from './pages/dashboard/Expense';
import ExpenseDetails from './pages/dashboard/ExpenseDetails';
import ExpenseReview from './components/expense/ExpenseReview';
import Jobs from './pages/dashboard/Jobs';


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
      <ProtectedRoute>
        <DashboardLayout />
      </ProtectedRoute>
    ),
    children: [
      {
        index: true,
        element: <Dashboard />,
      },
      {
        path: 'travels',
        element: <Travels />,
      },
      {
        path: 'travels/:travelId',
        element: <TravelDetails />
      },
      {
        path: 'notifications',
        element: <NotificationsPage />
      },
      {
        path: 'expenses',
        element: <Expense />,
      },
      {
        path: 'expenses/:travelId',
        element: <ExpenseDetails />
      },
      {
        path: 'expenses/review/:travelId',
        element: <ExpenseReview />
      },
      {
        path: 'jobs',
        element: <Jobs />,
      },

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