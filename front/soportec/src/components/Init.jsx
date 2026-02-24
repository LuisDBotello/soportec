import { useState } from "react";

const API_URL = import.meta.env.VITE_API_URL;

export default function Init() {
  const [orgName, setOrgName] = useState("");
  const [adminName, setAdminName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  const handleInit = async (e) => {
    e.preventDefault();

    const response = await fetch(`${API_URL}/init`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        organizationName: orgName,
        adminName,
        email,
        password,
      }),
    });

    if (response.ok) {
      setMessage("Sistema inicializado correctamente");
    } else {
      setMessage("Error al inicializar el sistema");
    }
  };

  return (
    <div>
      <h2>Inicialización del Sistema</h2>
      <form onSubmit={handleInit}>
        <input
          type="text"
          placeholder="Nombre de la organización"
          value={orgName}
          onChange={(e) => setOrgName(e.target.value)}
          required
        />
        <input
          type="text"
          placeholder="Nombre del administrador"
          value={adminName}
          onChange={(e) => setAdminName(e.target.value)}
          required
        />
        <input
          type="email"
          placeholder="Correo"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Contraseña"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <button type="submit">Crear Organización</button>
      </form>

      <p>{message}</p>
    </div>
  );
}