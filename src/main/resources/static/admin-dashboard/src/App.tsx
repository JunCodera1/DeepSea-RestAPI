import AdminDashboard from "./page/AdminDashboard";
import { Route, Routes } from "react-router-dom";

function App() {
  return (
    <Routes>
      <Route path="/admin-dashboard/dist/" element={<AdminDashboard />} />
    </Routes>
  );
}

export default App;
