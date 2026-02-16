import { useState } from 'react';
import { X, Upload, FileText } from 'lucide-react';
import { expenseService } from '@/services/expenseService';
import { Button } from '@/components/ui/button';
import toast from 'react-hot-toast';

interface UploadProofModalProps {
  expenseId: number;
  onClose: () => void;
  onSuccess: () => void;
}

const UploadProofModal = ({ expenseId, onClose, onSuccess }: UploadProofModalProps) => {
  const [file, setFile] = useState<File | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      const selectedFile = e.target.files[0];

      if (selectedFile.size > 10 * 1024 * 1024) {
        setError('File size must be less than 10MB');
        return;
      }

      const allowedTypes = ['application/pdf', 'image/jpeg', 'image/jpg', 'image/png'];
      if (!allowedTypes.includes(selectedFile.type)) {
        setError('Only PDF, JPG, and PNG files allowed');
        return;
      }

      setFile(selectedFile);
      setError('');
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!file) {
      setError('Please select a file');
      return;
    }

    setLoading(true);

    try {
      await expenseService.uploadProof(expenseId, file);
      onSuccess();
    } catch (error: any) {
      const message = error.response?.data?.message || 'Failed to upload proof';
      toast.error(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black/40 bg-opacity-30 flex items-center justify-center z-50 p-4">
      <div className="bg-white w-full max-w-md">
        <div className="flex justify-between items-center p-6 border-b border-gray-200">
          <h2 className="text-xl font-bold text-gray-900">Upload Proof Document</h2>
          <button onClick={onClose} className="text-gray-400 hover:text-gray-600">
            <X className="w-6 h-6" />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="p-6 space-y-6">
          <div className="border-2 border-gray-300 p-8 text-center hover:border-green-600 transition-colors">
            <input
              type="file"
              onChange={handleFileChange}
              accept=".pdf,.jpg,.jpeg,.png"
              className="hidden"
              id="proof-upload"
            />
            <label htmlFor="proof-upload" className="cursor-pointer">
              <Upload className="w-12 h-12 text-gray-400 mx-auto mb-3" />
              <p className="text-sm text-gray-600 mb-1">Click to upload receipt or proof</p>
              <p className="text-xs text-gray-500">PDF, JPG, PNG (max 10MB)</p>
            </label>

            {file && (
              <div className="mt-4 flex items-center justify-center space-x-2 text-roima700 bg-green-50 p-3">
                <FileText className="w-5 h-5" />
                <span className="text-sm font-medium">{file.name}</span>
              </div>
            )}
          </div>

          {error && <p className="text-sm text-red-600">{error}</p>}
        </form>

        <div className="flex justify-end space-x-3 p-6 border-t border-gray-200">
          <Button onClick={onClose} variant="outline" className="border-gray-300 text-gray-700">
            Cancel
          </Button>
          <Button onClick={handleSubmit} disabled={loading || !file} className="bg-green-700 hover:bg-green-800 text-white">
            {loading ? 'Uploading...' : 'Upload Proof'}
          </Button>
        </div>
      </div>
    </div>
  );
};

export default UploadProofModal;
