export const EmployeeCard = ({ employee, isSelected, onClick }: any) => {

  const initials = `${employee.firstName[0]}${employee.lastName[0]}`.toUpperCase();

  return (
    <button
      onClick={onClick}
      className={`w-72 text-left transition-all ${
        isSelected
          ? 'border-4 border-green-600 bg-green-50 shadow-xl'
          : 'border-2 border-gray-300 hover:border-green-500 hover:shadow-lg bg-white'
      }`}
    >
      <div className="p-4">
        <div className="flex items-start gap-3">
          {employee.profilePhotoUrl ? (
            <img src={employee.profilePhotoUrl} alt="" className="w-14 h-14 object-cover" />
          ) : (
            <div className="w-14 h-14 bg-green-600 flex items-center justify-center text-white font-bold text-lg">
              {initials}
            </div>
          )}

          <div className="flex-1 min-w-0">
            <h3 className="font-bold text-gray-900 text-base truncate">
              {employee.firstName} {employee.lastName}
            </h3>
            <p className="text-sm text-gray-700 truncate mt-0.5">{employee.designation}</p>
            <p className="text-xs text-gray-500 truncate mt-0.5">{employee.department}</p>
            <p className="text-xs text-gray-500 truncate mt-0.5">{employee.email}</p>
          </div>
        </div>

        {isSelected && (
          <div className="mt-3">
            <span className="px-2 py-1 bg-green-600 text-white text-xs font-bold">VIEWING</span>
          </div>
        )}
       
      </div>
    </button>
  );
};