
import { Link, useNavigate } from "react-router-dom";

export default function Navbar() {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");

  function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
    localStorage.removeItem("role");
    navigate("/login");
    window.location.reload();
  }

  return (
    <nav className="navbar">
      <Link className="brand" to="/">TestPortal</Link>

      <div className="nav-links">
        <Link to="/">Home</Link>

        {!token && <Link to="/login">Login</Link>}
        {!token && <Link to="/register">Register</Link>}

        <Link to="/results">Results</Link>

        {token && <button onClick={logout}>Logout</button>}
      </div>
    </nav>
  );
}
