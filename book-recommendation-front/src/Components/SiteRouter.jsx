import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Login from "../LoginRegister/Login";
import BookList from "./BookList";
import Register from "../LoginRegister/Register";
import AdminAddBook from "./AdminAddBook";
import AdminAddCategory from "./AdminAddCategory";

const SiteRouter = () => {
  
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/books" element={<BookList />} />
        <Route path="/addBook" element={<AdminAddBook/>}/>
        <Route path="/addCategory" element={<AdminAddCategory/>}/>
        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </BrowserRouter>
  );
};

export default SiteRouter;
