import "./App.css";
import AdminDashboard from "./page/AdminDashboard";
import { Route, Routes } from "react-router-dom";

function App() {
  return (
    <Routes>
      <Route path="/admin-dashboard/dist/admin" element={<AdminDashboard />} />
      {/* Các route khác nếu có */}
    </Routes>
  );
}

export default App;
