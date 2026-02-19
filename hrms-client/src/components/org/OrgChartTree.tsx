import type { OrgChartNode, OrgChartChild } from '@/types/orgChart';
import { EmployeeCard } from './EmployeeCard';

interface Props {
  hierarchy: OrgChartNode[];
  children: OrgChartChild[];
  selectedUserId: number | null;
  onEmployeeClick: (userId: number) => void;
}

const OrgChartTree = ({ hierarchy, children, selectedUserId, onEmployeeClick }: Props) => {

  return (
    <div className="bg-white border border-gray-200 p-8 overflow-x-auto max-w-full">
      <div className="inline-flex flex-col items-center min-w-max pl-20">
        {hierarchy.map((emp, index) => {
          const isSelected = emp.userId === selectedUserId;
        //   console.log("index :",index);
        //   console.log("length :",hierarchy.length);
          const hasNext = index < hierarchy.length - 1;

          return (
            <div key={emp.userId} className="flex flex-col items-center">
              <EmployeeCard
                employee={emp}
                isSelected={isSelected}
                onClick={() => onEmployeeClick(emp.userId)}
              />

              {hasNext && <div className="w-px h-8 bg-gray-400"></div>}
            </div>
          );
        })}

        {children.length > 0 && (
          <>
          <div className="w-px h-8 bg-gray-400"></div>
          <div className='border-2 max-w-fit'>
            <p className='text-center font-bold p-2'>Employee who direct reports</p>
            <div className="flex items-start p-8" style={{ gap: '3rem' }}>
              {children.map((child) => (
                  <div key={child.userId} className="flex flex-col items-center">
                  
                  <EmployeeCard
                    employee={child}
                    isSelected={false}
                    onClick={() => onEmployeeClick(child.userId)}
                    />
                </div>
              ))}
            </div>
            </div>
          </>
        )}
      </div>
    </div>
  );
};



export default OrgChartTree;
