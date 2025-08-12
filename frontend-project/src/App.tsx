import { BrowserRouter, Route, Routes } from "react-router-dom"
import Home from "./routes/Home"
import Catalog from "./routes/Home/Catalog"
import Admin from "./routes/Home/Admin"
import HomeBody from "./routes/Home/HomeBody"

function App() {

  return (
    <>
<BrowserRouter>
    <Routes>
      <Route path="/" element={<Home/>}>
        <Route index element={<HomeBody/>} />
        <Route path="/home" element={<HomeBody/>}/>
        <Route path="/catalog" element={<Catalog/>}/>
        <Route path="/admin" element={<Admin/>}/>
        </Route>

    </Routes>

</BrowserRouter>    
    </>
  )
}

export default App
