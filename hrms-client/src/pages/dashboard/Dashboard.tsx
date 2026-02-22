import {Card, CardContent, CardDescription, CardHeader, CardTitle} from '../../components/ui/card';

const Dashboard = () => {

  return (
    <div>
      <h1 className="text-2xl font-bold text-gray-900 mb-6">Dashboard</h1>

      <Card>
        <CardHeader>
            <CardTitle>
                Welcome to ROIMA HRMS
            </CardTitle>

            <CardDescription>
                Manage Everything Efficiently Here !
            </CardDescription>
        </CardHeader>
        <CardContent>
            
        </CardContent>
      </Card>
    </div>
  );
};

export default Dashboard;


