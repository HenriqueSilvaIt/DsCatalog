
import { NavLink } from 'react-router-dom';
import { Link } from 'react-router-dom';
import './style.css';
import menuIcon from '../../assets/Union.svg';
import { useState } from 'react';

export default function HeaderDCatalog() {

    const [isMenuOpen, setIsMenuOpen] = useState(false);


    function handleToggleMenu() {

        setIsMenuOpen(!isMenuOpen);

    }

    return (
        <header >
            <div className=" dscatalog-container header-content-container ">

                <Link to="/" >
                    <h1> DS Catalog</h1>

                </Link>

                <nav className={`dscatalog-nav-bar ${isMenuOpen ? 'show-menu' : ''}`} >
                    <NavLink to="/home" className={({ isActive }) => isActive ? "menu-item menu-active" : "menu-item"}>
                        HOME
                    </NavLink>
                    <NavLink to="/catalog" className={({ isActive }) => isActive ? "menu-item menu-active" : "menu-item"}>
                        CÁTALOGO
                    </NavLink>
                    <NavLink to="/admin" className={({ isActive }) => isActive ? "menu-item menu-active" : "menu-item"}>
                        ADMIN
                    </NavLink>
                </nav>

                    <div className="dscatalog-menu-icon" >

                        <button onClick={handleToggleMenu} > <img src={menuIcon} alt="menuIcon" />  </button>

                    </div >

            </div>
        </header>
    );
}