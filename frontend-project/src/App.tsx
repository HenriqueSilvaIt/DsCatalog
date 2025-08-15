import {  Route, Routes } from "react-router-dom"
import Home from "./routes/Home"
import Catalog from "./routes/Home/Catalog"
import HomeBody from "./routes/Home/HomeBody"
import ProductDetails from "./routes/Home/ProductDetails"
import { history } from './utils/history.ts';
import { unstable_HistoryRouter as HistoryRouter } from 'react-router-dom';

function App() {

  return (
    <>
<HistoryRouter history={history}>
    <Routes>
      <Route path="/" element={<Home/>}>
        <Route index element={<HomeBody/>} />
        <Route path="home" element={<HomeBody/>}/>
        <Route path="catalog" element={<Catalog/>}/>
            <Route path="product-details/:productId" element={<ProductDetails />} />

        </Route>
    </Routes>
</HistoryRouter>    
    </>
  )
}

export default App
