import { useState } from 'react';
import './style.css';

type Props = {

    onSearch: Function;

}



export default function SearchCatalogBar({ onSearch }: Props) {

    const [text, setText] = useState<string>("");

    function handleClickReset() {
        setText('');
        onSearch(text);
    }

    function handleInputChange(event: any) {
     
        setText(event.target.value);
    }

    function handleFormSubmit(event: any) {
        event.preventDefault();
        onSearch(text);
    }


    return (
        <>

            <div className="dscatalog-search-bar-container dscatalog-container">
                <h2>Catálogo de Produtos</h2>

                <div className="dscatalog-serach-bar-details-container">


                    <form onSubmit={handleFormSubmit}
                        className="dscatalog-input-search">
                        <input value={text} type="text" placeholder="Nome do produto" onChange={handleInputChange} />
                        <button type="submit">🔎︎</button>
                    </form>
                    <div className="dscatalog-bar-category">
                        <select className="dscatalog-select">
                            <option value="Categoria" > Categoria</option>
                            <option value="otherOption">Other option</option>
                        </select>
                        <button name="reset" onClick={handleClickReset} >LIMPAR FILTRO</button>
                    </div>
                </div>

            </div>

        </>
    );
}