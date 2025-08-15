
import './style.css';
import { Link } from 'react-router-dom';
import type { ProductDTO } from '../../models/product';

type Props = {

    product: ProductDTO;
}

export default function ProductDetailsCard({product}: Props) {

    return (

        <>
            <div className="dscatalog-product-details-container">
                <div className="dscatalog-product-details-button">
                    <Link to="/catalog">
                        <button>   <span>{'<'}</span>VOLTAR</button>
                    </Link>
                </div>
                <div className="dscatalog-product-details-card-container">
                    <div className="dscatalog-product-details-card">
                        <div className="dscatalog-product-details-img ">
                            <img src={product.imgUrl} alt={product.name}></img>
                        </div>

                        <div className="dscatalog-product-details-title">
                            <h2>{product.name}</h2>

                            <div className="dscatalog-product-details-price">
                                <p>R$</p>
                                <h4>{product.price.toFixed(2)}</h4>
                            </div>
                        </div>
                    </div>

                    <div className="dscatalog-product-details-description">
                        <p ><strong>Descrição do produto</strong></p>
                        <br></br>

                        <p>{product.description} </p>
                    </div>
                </div>
            </div>

        </>
    );


}