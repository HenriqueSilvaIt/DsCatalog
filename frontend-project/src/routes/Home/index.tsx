import { Outlet } from "react-router-dom";
import HeaderDCatalog from "../../components/header";

export default function Home() {

    return (

        <>
        <HeaderDCatalog/>
            <Outlet />
</>
        

    );
}