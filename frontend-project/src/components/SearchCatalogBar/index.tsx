import './style.css';

export default function SearchCatalogBar() {
    return (
        <>

            <div className="dscatalog-search-bar-container dscatalog-container">
                <h2>Catálogo de Produtos</h2>

                <div className="dscatalog-serach-bar-details-container">

                    <div className="dscatalog-input-search">
                        <input placeholder="Nome do produto"></input>
                        <button type="submit">🔎︎</button>
                    </div>
                    <div className="dscatalog-bar-category">
                        <select className="dscatalog-select">
                            <option value="Categoria" > Categoria</option>
                            <option value="otherOption">Other option</option>
                       </select>
                        <button> LIMPAR FILTRO</button>
                    </div>
                </div>

            </div>

        </>
    );
}