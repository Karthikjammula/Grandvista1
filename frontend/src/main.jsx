import React,{useEffect,useState} from 'react';
import {createRoot} from 'react-dom/client';
import './style.css';

const API=import.meta.env.VITE_API_URL||'http://localhost:8080';
const token=()=>localStorage.getItem('token');
async function api(path,opts={}){
 const r=await fetch(API+path,{...opts,headers:{'Content-Type':'application/json',...(token()?{Authorization:'Bearer '+token()}:{}),...(opts.headers||{})}});
 let d={}; try{d=await r.json()}catch{}
 if(!r.ok)throw new Error(d.message||'Request failed'); return d;
}
function App(){
 const [rooms,setRooms]=useState([]),[bookings,setBookings]=useState([]);
 const [email,setEmail]=useState(''),[password,setPassword]=useState(''),[msg,setMsg]=useState('');
 const [logged,setLogged]=useState(!!token());
 const role=localStorage.getItem('role');
 const load=async()=>{
  try{setRooms(await api('/api/rooms'))}catch(e){setMsg(e.message)}
  if(token())try{setBookings(await api('/api/bookings/my'))}catch{}
 };
 useEffect(()=>{load()},[logged]);
 async function login(e){e.preventDefault();try{
  const d=await api('/api/auth/login',{method:'POST',body:JSON.stringify({email,password})});
  localStorage.setItem('token',d.token);localStorage.setItem('role',d.role);setLogged(true);setMsg('Login successful');
 }catch(e){setMsg(e.message)}}
 async function book(r){
  const checkIn=prompt('Check-in (YYYY-MM-DD)','2026-10-01'),checkOut=prompt('Check-out (YYYY-MM-DD)','2026-10-02');
  if(!checkIn||!checkOut)return;
  try{
   const b=await api('/api/bookings',{method:'POST',body:JSON.stringify({roomId:r.id,checkIn,checkOut,amount:r.price})});
   await api('/api/payments/'+b.id,{method:'POST',body:JSON.stringify({amount:r.price,method:'CARD'})});
   setMsg('Booking and demo payment successful');load();
  }catch(e){setMsg(e.message)}
 }
 function logout(){localStorage.clear();setLogged(false);setBookings([])}
 return <div className="app">
  <header><h1>GrandVista</h1><p>Hospitality Inventory & Dynamic Room Reservation Platform</p>{logged&&<button onClick={logout}>Logout</button>}</header>
  {!logged?<form className="card login" onSubmit={login}><h2>Login</h2>
   <input placeholder="Email" value={email} onChange={e=>setEmail(e.target.value)} required/>
   <input placeholder="Password" type="password" value={password} onChange={e=>setPassword(e.target.value)} required/>
   <button>Login</button><small>Demo: user@grandvista.com / User@123</small>
  </form>:
  <main><div className="notice">{msg}</div><h2>Rooms</h2>
   <div className="grid">{rooms.map(r=><div className="card" key={r.id}><h3>{r.type}</h3><p>Room {r.roomNumber}</p><p>₹{r.price}/night</p><p>{r.available?'Available':'Reserved'}</p>{r.available&&<button onClick={()=>book(r)}>Book Now</button>}</div>)}</div>
   <h2>My Bookings</h2><div className="card">{bookings.length?bookings.map(b=><p key={b.id}>#{b.id} • Room {b.roomId} • {b.status} • ₹{b.amount}</p>):'No bookings yet.'}</div>
   {role==='ADMIN'&&<p className="admin">Admin logged in. Use Postman for room CRUD and admin APIs.</p>}
  </main>}
 </div>
}
createRoot(document.getElementById('root')).render(<App/>);
