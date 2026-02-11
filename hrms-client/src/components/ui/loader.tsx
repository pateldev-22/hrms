const Loader = ({fullScreen = true}) => {
  if (fullScreen) {
    return (
      <div className="fixed inset-0 bg-white flex items-center justify-center z-50">
        <div className="text-center">
          <div className="w-16 h-16 border-4 border-roima-200 border-t-roima-600 rounded-full animate-spin mx-auto"></div>
          <p className="mt-4 text-gray-600 font-medium">Loading...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="flex items-center justify-center py-12">
      <div className="w-12 h-12 border-4 border-roima-200 border-t-roima-600 rounded-full animate-spin"></div>
    </div>
  );
};

export default Loader;