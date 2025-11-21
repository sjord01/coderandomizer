import React, { useState } from "react";

const API = "/api/codes";

export default function App() {
    const [current, setCurrent] = useState(null);
    const [message, setMessage] = useState("");

    async function generate() {
        setMessage("");
        try {
            const res = await fetch(`${API}/generate`, { method: "POST" });
            if (!res.ok) {
                // try to get a text body (stacktrace or message)
                const errText = await res.text();
                setMessage(`Server error ${res.status}: ${errText || res.statusText}`);
                return;
            }
            const data = await res.json();
            setCurrent(data);
        } catch (err) {
            setMessage(`Network error: ${err.message}`);
        }
    }

    async function test() {
        setMessage("");
        if (!current) return;
        try {
            const res = await fetch(`${API}/${current.id}/test`, { method: "POST" });
            const bodyText = await res.text();
            // try parse JSON if possible, otherwise show raw text
            try {
                const json = JSON.parse(bodyText);
                setMessage(json.message || JSON.stringify(json));
            } catch {
                setMessage(bodyText || `Status ${res.status}`);
            }
            // refresh current
            const updated = await fetch(`${API}`).then(r => r.json()).then(list => list.find(l => l.id === current.id));
            if (updated) setCurrent(updated);
        } catch (err) {
            setMessage(`Network error: ${err.message}`);
        }
    }

    async function cleanup() {
        setMessage("");
        if (!current) return;
        try {
            const res = await fetch(`${API}/${current.id}/cleanup`, { method: "POST" });
            const bodyText = await res.text();
            try {
                const json = JSON.parse(bodyText);
                setMessage(json.message || JSON.stringify(json));
            } catch {
                setMessage(bodyText || `Status ${res.status}`);
            }
            const updatedList = await fetch(`${API}`).then(r=>r.json());
            const found = updatedList.find(l => l.id === current.id);
            if (found) setCurrent(found);
        } catch (err) {
            setMessage(`Network error: ${err.message}`);
        }
    }

    async function printCsv() {
        try {
            const res = await fetch(`${API}/export`);
            if (!res.ok) {
                const errText = await res.text();
                setMessage(`Server error ${res.status}: ${errText || res.statusText}`);
                return;
            }
            const text = await res.text();
            const blob = new Blob([text], { type: 'text/csv' });
            const url = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = 'codes_export.csv';
            a.click();
            URL.revokeObjectURL(url);
        } catch (err) {
            setMessage(`Network error: ${err.message}`);
        }
    }

    return (
        <div className="container">
            <h2>Maka UI - Code Generator</h2>
            <div>
                <button className="btn" onClick={generate}>Generate</button>
            </div>
            <div style={{marginTop:12}}>
                <div className="code-box">{current ? current.code : "No code generated yet"}</div>
            </div>
            <div style={{marginTop:12}}>
                <button className="btn" onClick={test} disabled={!current}>Test</button>
                <button className="btn" onClick={cleanup} disabled={!current}>Clean Up Code</button>
            </div>
            {message && (
                <div className={message.toLowerCase().includes("good") || message.toLowerCase().includes("cleaned") ? "success" : "error"}>
                    {message}
                </div>
            )}
            <div style={{marginTop:20}}>
                <button className="btn" onClick={printCsv}>Print CSV</button>
            </div>
        </div>
    );
}