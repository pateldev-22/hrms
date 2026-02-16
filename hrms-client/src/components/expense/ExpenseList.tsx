// import { useState, useEffect } from 'react';
// import axios from 'axios';
// import type { Expense } from '@/types/expenses';

// const ExpenseList = ({travelId,fetchExpense}) => {
    

//     useEffect(() => {
//         fetchExpense();
//     }, []);



//     if (loading) return <div>Loading...</div>;

//     return (
//         <div className="expense-list">
//             <div className='flex flex-row justify-between mb-8'>
//             <h2>My Expenses</h2>
//             </div>

//             <table >
//                 <thead className='gap-x-3 m-3'>
//                     <tr>
//                         <th>Date</th>
//                         <th>Travel</th>
//                         <th>Category</th>
//                         <th>Amount</th>
//                         <th>Description</th>
//                         <th>Status</th>
//                         <th>Actions</th>
//                     </tr>
//                 </thead>
//                 <tbody>
//                     {expenses.map(expense => (
//                         <tr key={expense.expenseId}>
//                             <td>{expense.expenseDate}</td>
//                             <td>{expense.hrRemark}</td>
//                             <td>{expense.categoryName}</td>
//                             <td>₹{expense.amount}</td>
//                             <td>{expense.description}</td>
//                             <td>
//                                 <span>
//                                     {expense.statusDisplayName}
//                                 </span>
//                             </td>
//                             <td>
//                                 {expense.canEdit && (
//                                     <button onClick={() => window.location.href = `/expenses/edit/${expense.expenseId}`}>
//                                         Edit
//                                     </button>
//                                 )}
//                                 {expense.canDelete && (
//                                     <button onClick={() => handleDelete(expense.expenseId)}>
//                                         Delete
//                                     </button>
//                                 )}
//                             </td>
//                         </tr>
//                     ))}
//                 </tbody>
//             </table>
//         </div>
//     );

//     async function handleDelete(expenseId) {
//         if (!window.confirm('Are you sure you want to delete this expense?')) return;

//         try {
//             const token = localStorage.getItem('accessToken');
//             await axios.delete(`/api/expenses/${expenseId}`, {
//                 headers: { 'Authorization': `Bearer ${token}` }
//             });
//             fetchExpenses(); 
//         } catch (error) {
//             alert('Error deleting expense: ' + error.response?.data?.message);
//         }
//     }
// };

// export default ExpenseList;
