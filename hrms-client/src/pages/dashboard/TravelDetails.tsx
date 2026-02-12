import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { ArrowLeft, MapPin, Calendar, FileText, CheckCircle, Upload, Download } from 'lucide-react';
import { travelService } from '@/services/travelService';
import { useAuth } from '@/context/AuthContext';
import type { TravelPlan, TravelDocument } from '@/types/travels';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import DocumentUploadModal from '@/components/travel/DocumentUploadModal';
import { formatDate } from '@/utils/dateFormatter';
import toast from 'react-hot-toast';

const TravelDetails = () => {
  const { travelId } = useParams<{ travelId: string }>();
  const navigate = useNavigate();
  const { user, hasRole } = useAuth();

  const [travel, setTravel] = useState<TravelPlan | null>(null);
  const [documents, setDocuments] = useState<TravelDocument[]>([]);
  const [loading, setLoading] = useState(true);
  const [showUploadModal, setShowUploadModal] = useState(false);

  const isHR = hasRole('HR');
  const isAssigned = travel?.assignments.some((a) => a.employeeEmail === user);

  useEffect(() => {
    if (travelId) {
      fetchTravelDetails();
      fetchDocuments();
    }
  }, [travelId]);

  const fetchTravelDetails = async () => {
    try {
      const response = await travelService.getTravelById(Number(travelId));
      setTravel(response.data);
    } catch (error) {
      toast.error('Failed to load travel details');
      navigate('/travels');
    } finally {
      setLoading(false);
    }
  };

  const fetchDocuments = async () => {
    try {
      const response = await travelService.getTravelDocuments(Number(travelId));
      setDocuments(response.data);
    } catch (error) {
      console.error('Error fetching documents:', error);
    }
  };

  const handleAcknowledge = async () => {
    try {
      await travelService.acknowledgeTravelAssignment(Number(travelId));
      toast.success('Travel assignment acknowledged!');
      fetchTravelDetails();
    } catch (error) {
      toast.error('Failed to acknowledge assignment');
    }
  };

  const handleDocumentUploaded = () => {
    setShowUploadModal(false);
    fetchDocuments();
    fetchTravelDetails();
    toast.success('Document uploaded successfully!');
  };

  const handleDownload = (doc: TravelDocument) => {
    window.open(doc.fileUrl);
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="w-12 h-12 border-4 border-green-200 border-t-green-700 animate-spin"></div>
      </div>
    );
  }

  if (!travel) {
    return null;
  }

  console.log(travel);
  const userAssignment = travel.assignments.find((a) => a.employeeEmail === user);
  console.log("here -> ",userAssignment);
  const canAcknowledge = userAssignment && userAssignment.assignmentStatus === 'ASSIGNED';
  console.log("now ->",canAcknowledge);

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div className="flex items-center space-x-4">
          <button onClick={() => navigate('/travels')} className="text-gray-600 hover:text-gray-900">
            <ArrowLeft className="w-6 h-6" />
          </button>
          <div>
            <h1 className="text-2xl font-bold text-gray-900">{travel.travelName}</h1>
            <p className="text-gray-600 mt-1">Travel Details</p>
          </div>
        </div>

        {canAcknowledge && (
        <div className='flex flex-col'>
            <span className='text-red-500'>You have not acknowledge the assignment yet</span>
            
          <Button onClick={handleAcknowledge} className="bg-green-700 hover:bg-green-800 text-white">
            Click Here To Acknowledge Assignment
          </Button>
        </div>
        )}
      </div>

      <Card className="border border-gray-200">
        <CardHeader className="border-b border-gray-100">
          <CardTitle>Travel Information</CardTitle>
        </CardHeader>
        <CardContent className="pt-6 space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <div className="flex items-center text-gray-700 mb-2">
                <MapPin className="w-5 h-5 mr-2 text-gray-400" />
                <span className="font-medium">Destination</span>
              </div>
              <p className="text-gray-900 ml-7">{travel.destination}</p>
            </div>

            <div>
              <div className="flex items-center text-gray-700 mb-2">
                <Calendar className="w-5 h-5 mr-2 text-gray-400" />
                <span className="font-medium">Duration</span>
              </div>
              <p className="text-gray-900 ml-7">
                {formatDate(travel.startDate)} - {formatDate(travel.endDate)}
              </p>
            </div>

            {travel.purpose && (
              <div className="md:col-span-2">
                <div className="flex items-center text-gray-700 mb-2">
                  <FileText className="w-5 h-5 mr-2 text-gray-400" />
                  <span className="font-medium">Purpose</span>
                </div>
                <p className="text-gray-900 ml-7">{travel.purpose}</p>
              </div>
            )}

            <div className="md:col-span-2">
              <p className="text-sm text-gray-500">
                Created by {travel.createdByName} on {formatDate(travel.createdAt)}
              </p>
            </div>
          </div>
        </CardContent>
      </Card>

      <Card className="border border-gray-200">
        <CardHeader className="border-b border-gray-100">
          <div className="flex justify-between items-center">
            <CardTitle>Assigned Employees ({travel.assignments.length})</CardTitle>
          </div>
        </CardHeader>
        <CardContent className="pt-4">
          <div className="space-y-3">
            {travel.assignments.map((assignment) => (
              <div
                key={assignment.assignmentId}
                className="flex items-center justify-between p-3 border border-gray-200 hover:border-green-600 transition-colors"
              >
                <div className="flex items-center space-x-3">
                  <div className="w-10 h-10 bg-green-100 flex items-center justify-center text-green-700 font-semibold">
                    {assignment.employeeName
                      .split(' ')
                      .map((n) => n[0])
                      .join('')}
                  </div>
                  <div>
                    <p className="font-medium text-gray-900">{assignment.employeeName}</p>
                    <p className="text-sm text-gray-500">
                      {assignment.employeeEmail} • {assignment.department}
                    </p>
                  </div>
                </div>

                <span
                  className={`px-3 py-1 text-xs font-medium ${
                    assignment.assignmentStatus === 'ACKNOWLEDGED'
                      ? 'bg-green-100 text-green-800'
                      : assignment.assignmentStatus === 'ASSIGNED'
                      ? 'bg-blue-100 text-blue-800'
                      : 'bg-gray-100 text-gray-800'
                  }`}
                >
                  {assignment.assignmentStatus}
                </span>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>

      <Card className="border border-gray-200">
        <CardHeader className="border-b border-gray-100">
          <div className="flex justify-between items-center">
            <CardTitle>Documents ({documents.length})</CardTitle>
            {(isHR || isAssigned) && (
              <Button
                onClick={() => setShowUploadModal(true)}
                className="bg-green-700 hover:bg-green-800 text-white text-sm"
              >
                <Upload className="w-4 h-4 mr-2" />
                Upload Document
              </Button>
            )}
          </div>
        </CardHeader>
        <CardContent className="pt-4">
          {documents.length === 0 ? (
            <div className="text-center py-8 text-gray-500">
              <FileText className="w-12 h-12 mx-auto mb-2 text-gray-300" />
              <p>No documents uploaded yet</p>
            </div>
          ) : (
            <div className="space-y-3">
              {documents.map((doc) => (
                <div
                  key={doc.travelDocumentId}
                  className="flex items-center justify-between p-3 border border-gray-200 hover:border-green-600 transition-colors"
                >
                  <div className="flex items-center space-x-3 flex-1">
                    <div className="w-10 h-10 bg-green-100 flex items-center justify-center">
                      <FileText className="w-5 h-5" />
                    </div>
                    <div className="flex-1 min-w-0">
                      <p className="font-medium text-gray-900 truncate">{doc.fileName}</p>
                      <p className="text-sm text-gray-500">
                        {doc.documentType} • {doc.readableFileSize} • Uploaded by {doc.uploadedByName}
                      </p>
                      <p className="text-xs text-gray-400">{formatDate(doc.uploadedAt)}</p>
                    </div>
                  </div>

                  <Button
                    onClick={() => handleDownload(doc)}
                    className="ml-4 bg-gray-100 hover:bg-gray-200 text-gray-700 text-sm"
                  >
                    <Download className="w-4 h-4" />
                  </Button>
                </div>
              ))}
            </div>
          )}
        </CardContent>
      </Card>

      {showUploadModal && (
        <DocumentUploadModal
          travelId={Number(travelId)}
          onClose={() => setShowUploadModal(false)}
          onSuccess={handleDocumentUploaded}
        />
      )}
    </div>
  );
};

export default TravelDetails;
