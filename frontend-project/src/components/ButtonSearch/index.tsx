import './style.css';
import signIcon from '../../assets/Seta.png';
import { Link } from 'react-router-dom';

export default function ButtonSeach() {

    return (
        <>

            <Link to="/catalog">
                <button className="dscatalog-button-search">INICIE AGORA SUA BUSCA</button>
            </Link>
            <Link to="/catalog">
                <button className="dsctalog-btn-submit"> <img className="dscatalog-btn-img" src={signIcon} /> </button>

            </Link>

        </>
    );
}