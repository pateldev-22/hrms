import { useEffect, useState } from 'react';
import { X, Upload, FileText } from 'lucide-react';
import { travelService } from '@/services/travelService';
import { Button } from '@/components/ui/button';
import toast from 'react-hot-toast';
import { useAuth } from '@/context/AuthContext';
import type { User } from '@/types/User';
import api from '@/services/api';

interface DocumentUploadModalProps {
  travelId: number;
  onClose: () => void;
  onSuccess: () => void;
}

const DocumentUploadModal = ({ travelId, onClose, onSuccess }: DocumentUploadModalProps) => {
  const [file, setFile] = useState<File | null>(null);
  const [documentType, setDocumentType] = useState('');
  const [loading, setLoading] = useState(false);
  const [errors, setErrors] = useState<{ [key: string]: string }>({});
  const {hasRole} = useAuth();
  const isHR = hasRole('HR');
  const [employees, setEmployees] = useState<User[]>([]);
  const [selectedEmployeesId,setSelectedEmployeesId] = useState();
  const [userId,setUserId] = useState();
  useEffect(() => {
    fetchEmployees();
  }, []);

  const fetchEmployees = async () => {
    try {
      const response = await api.get<User[]>('/user');
      setEmployees(response.data);
      console.log(response.data);
    } catch (error) {
      toast.error('Failed to load employees');
    }
  };



  const documentTypes = ['VISA', 'TICKET', 'BOARDING_PASS', 'ACCOMMODATION', 'INSURANCE', 'POLICY', 'OTHER'];

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      const selectedFile = e.target.files[0];

      if (selectedFile.size > 10 * 1024 * 1024) {
        setErrors({ file: 'File size must be less than 10MB' });
        return;
      }

      const allowedTypes = ['application/pdf', 'image/jpeg', 'image/jpg', 'image/png'];
      if (!allowedTypes.includes(selectedFile.type)) {
        setErrors({ file: 'Only PDF, JPG, and PNG files are allowed' });
        return;
      }

      setFile(selectedFile);
      setErrors({});
    }
  };

  const validate = () => {
    const newErrors: { [key: string]: string } = {};

    if(isHR){
        if(!userId) newErrors.employee = 'Please select a employee'
    }
    if (!file) newErrors.file = 'Please select a file';
    if (!documentType) newErrors.documentType = 'Please select document type';

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleEmployeeChange = (e : any) => {
    const emp = e.target.value;
    const user = employees.find((ep) => (ep.email === emp));
    const userId = user.userId;
    console.log(userId);
    setSelectedEmployeesId(emp);
    setUserId(userId);
  };

  const handleSubmit = async (e: any) => {
    e.preventDefault();

    if (!validate() || !file) return;

    setLoading(true);

    try {
      await travelService.uploadDocument(travelId, file, documentType,userId);
      onSuccess();
    } catch (error: any) {
      const message = error.response?.data?.message || 'Failed to upload document';
      console.log(message);
      toast.error(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-opacity-30 backdrop-blur-sm flex items-center justify-center z-50 p-4">
      
      <div className="bg-green-50 w-full max-w-md border-2 border-black">
        <div className="flex justify-between items-center p-6 border-b border-gray-200">
          <h2 className="text-xl font-bold text-gray-900">Upload Document</h2>
          <button onClick={onClose} className="text-gray-400 hover:text-gray-600 transition-colors">
            <X className="w-6 h-6" />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="p-6 space-y-6">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Document Type *</label>
            <select
              value={documentType}
              onChange={(e) => setDocumentType(e.target.value)}
              className={`w-full px-3 py-2 border ${
                errors.documentType ? 'border-red-500' : 'border-gray-300'
              } focus:outline-none focus:ring-2 focus:ring-green-600`}
            >
              <option value="">Select document type</option>
              {documentTypes.map((type) => (
                <option key={type} value={type}>
                  {type.replace('_', ' ')}
                </option>
              ))}
            </select>
            {errors.documentType && <p className="mt-1 text-sm text-red-600">{errors.documentType}</p>}
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Select File *</label>
            <div className="border-2 border-dashed border-gray-300 p-6 text-center hover:border-green-600 transition-colors">
              <input
                type="file"
                onChange={handleFileChange}
                accept=".pdf,.jpg,.jpeg,.png"
                className="hidden"
                id="file-upload"
              />
              <label htmlFor="file-upload" className="cursor-pointer">
                <Upload className="w-12 h-12 text-gray-400 mx-auto mb-2" />
                <p className="text-sm text-gray-600">Click to upload or drag and drop</p>
                <p className="text-xs text-gray-500 mt-1">PDF, JPG, PNG (max 10MB)</p>
              </label>

              {file && (
                <div className="mt-4 flex items-center justify-center space-x-2 text-green-700">
                  <FileText className="w-5 h-5" />
                  <span className="text-sm font-medium">{file.name}</span>
                </div>
              )}
            </div>
            {errors.file && <p className="mt-1 text-sm text-red-600">{errors.file}</p>}
          </div>

          <p className="text-xs text-gray-500">
            * Supported formats: PDF, JPG, PNG
            <br />* Maximum file size: 10MB
          </p>

        {isHR && 
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Select Employee For Whom This Document is For*</label>

              <select value={selectedEmployeesId} onChange={handleEmployeeChange}>
                <option value=" ">Select</option>
                    {employees.map((e) => (
                        <option>
                            {e.email}
                        </option>
                    ))}
              </select>
              {selectedEmployeesId && (
                    <p>
                    Selected Employee: <strong>{selectedEmployeesId}</strong>
                    </p>
                )}
            {errors.employee && <p className="mt-1 text-sm text-red-600">{errors.employee}</p>}
            </div>
        }
        </form>

        <div className="flex justify-end space-x-3 p-6 border-t border-gray-200">
          <Button type="button" variant={'destructive'} onClick={onClose} className="px-4 py-2 border border-gray-300 text-white hover:bg-red-500">
            Cancel
          </Button>
          <Button onClick={handleSubmit} disabled={loading} className="px-4 py-2 bg-green-700 hover:bg-green-800 text-white">
            {loading ? 'Uploading...' : 'Upload Document'}
          </Button>
        </div>
      </div>
    </div>
  );
};

export default DocumentUploadModal;
