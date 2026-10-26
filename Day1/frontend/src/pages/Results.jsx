
import { useEffect, useState } from "react";
import api from "../services/api";

const emptyForm = {
  studentName: "",
  subject: "",
  marks: "",
  totalMarks: "",
};

export default function Results() {
  const role = localStorage.getItem("role");
  const isAdmin = role === "ADMIN";

  const [results, setResults] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [error, setError] = useState("");

  async function loadResults() {
    try {
      const response = await api.get("/test-results");
      setResults(response.data);
    } catch (err) {
      setError(err.response?.data?.error || "Could not load results");
    }
  }

  useEffect(() => {
    loadResults();
  }, []);

  async function addResult(e) {
    e.preventDefault();
    setError("");

    try {
      await api.post("/test-results", {
        ...form,
        marks: Number(form.marks),
        totalMarks: Number(form.totalMarks),
      });

      setForm(emptyForm);
      await loadResults();
    } catch (err) {
      setError(err.response?.data?.error || "Could not add result");
    }
  }

  async function deleteResult(id) {
    if (!window.confirm("Delete this result?")) return;

    try {
      await api.delete(`/test-results/${id}`);
      await loadResults();
    } catch (err) {
      setError(err.response?.data?.error || "Could not delete result");
    }
  }

  return (
    <main className="results">
      <h2>Test Results</h2>

      {isAdmin && (
        <form className="result-form" onSubmit={addResult}>
          <h3>Add Test Result</h3>

          <input
            placeholder="Student name"
            required
            value={form.studentName}
            onChange={(e) =>
              setForm({ ...form, studentName: e.target.value })
            }
          />

          <input
            placeholder="Subject"
            required
            value={form.subject}
            onChange={(e) =>
              setForm({ ...form, subject: e.target.value })
            }
          />

          <input
            type="number"
            min="0"
            placeholder="Marks obtained"
            required
            value={form.marks}
            onChange={(e) =>
              setForm({ ...form, marks: e.target.value })
            }
          />

          <input
            type="number"
            min="0.01"
            placeholder="Total marks"
            required
            value={form.totalMarks}
            onChange={(e) =>
              setForm({ ...form, totalMarks: e.target.value })
            }
          />

          <button className="btn" type="submit">Add Result</button>
        </form>
      )}

      {error && <p className="error">{error}</p>}

      <div className="table-wrapper">
        <table>
          <thead>
            <tr>
              <th>Student</th>
              <th>Subject</th>
              <th>Marks</th>
              {isAdmin && <th>Action</th>}
            </tr>
          </thead>

          <tbody>
            {results.map((result) => (
              <tr key={result.id}>
                <td>{result.studentName}</td>
                <td>{result.subject}</td>
                <td>{result.marks} / {result.totalMarks}</td>

                {isAdmin && (
                  <td>
                    <button
                      className="delete-btn"
                      onClick={() => deleteResult(result.id)}
                    >
                      Delete
                    </button>
                  </td>
                )}
              </tr>
            ))}

            {results.length === 0 && (
              <tr>
                <td colSpan={isAdmin ? 4 : 3}>No results available.</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </main>
  );
}
