import './style.css';
import homeCard from '../../assets/home-card.svg';
import ButtonSeach from '../ButtonSearch';

export default function HomeCard() {

    return (
        <div className="dscatalog-card-container dscatalog-mg-top-20">
            <div className="dscatalog-card-position">
                <img className="dscatalog-img-card" src={homeCard} alt="homeCard" />

                <div className="dscatalog-card-deitals-container">
                    <h2>Conheça  o melhor <br /> catálogo de produtos</h2>
                    <p>Ajudaremos você a encontrar os melhores produtos disponíveis no mercado.</p>
                    <div className="dscatalog-btn-search">
                        <ButtonSeach />
                    </div>


                </div>
            </div>
        </div>
    );


}