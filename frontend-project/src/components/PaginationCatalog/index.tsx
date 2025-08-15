
import './style.css'

type Props = {

    totalPages: number;
    currentPage: number;
    onPageChange: (page: number) => void;
    lastPage: boolean;
}


export default function PaginationCatalog({ totalPages, currentPage, onPageChange, lastPage }: Props) {
    // A matriz 'pages' é criada vazia a cada chamada da função
    // para evitar que os números de página se acumulem.
    const pages: (number | '...')[] = [];
    const maxPagesToShow = 10;



const adjustedTotalPages = lastPage 
    ? currentPage 
    : Math.min(currentPage + 1, totalPages);


       // Mostra todas as páginas se for pequeno
    if (adjustedTotalPages <= maxPagesToShow) {
        for (let i = 1; i <= adjustedTotalPages; i++) {
            pages.push(i);
        }
    } else {
        // Começo da paginação
        if (currentPage <= 3) {
            for (let i = 1; i <= Math.min(3, adjustedTotalPages); i++) {
                pages.push(i);
            }
            if (!lastPage) {
                pages.push('...');
                pages.push(adjustedTotalPages);
            }
        }
        // Fim da paginação
        else if (lastPage) {
            pages.push(1);
            if (currentPage > 4) pages.push('...');
            for (let i = currentPage - 2; i <= currentPage; i++) {
                pages.push(i);
            }
        }
        // Meio da paginação
        else {
            pages.push(1);
            pages.push('...');
            pages.push(currentPage - 1);
            pages.push(currentPage);
            pages.push(currentPage + 1);
            pages.push('...');
            pages.push(adjustedTotalPages);
        }
    }



    return (
        <div className="dscatalog-pagination-container">
            {/* Botão de navegação 'Anterior' */}
            <button
                className="pagination-arrow"
                onClick={() => onPageChange(currentPage - 1)}
                disabled={currentPage === 1} // Desabilita se estiver na primeira página
            >
                {'<'}
            </button>

            {/* Mapeia a matriz de páginas para renderizar os botões ou as elipses. */}
            {pages.map((page, index) => {
                // Se o item for a elipse (...), renderiza como um <span>
                if (page === '...') {
                    return (
                        <span key={index} className="pagination-ellipsis">
                            {page}
                        </span>
                    );
                }

                // Verifica se a página é a página atual para aplicar o estilo 'active'
                const isActive = page === currentPage;
                const activeClass = `pagination-item ${isActive ? 'active' : ''}`;



                {        // Renderiza um botão para cada número de página
                    /* Chamada da função que renderiza os números de página */
                }
                return (


                    <button
                        key={index}
                        className={activeClass}
                        // Chama a função onPageChange com o número da página
                        onClick={() => onPageChange(Number(page))}
                        // Desabilita o botão se ele for a página atual
                        disabled={isActive}

                    >
                        {page}
                    </button>
                );
            })}



            {/* Botão de navegação 'Próximo' */}

            {!lastPage &&
                <button
                    className="pagination-arrow"
                    onClick={() => onPageChange(currentPage + 1)}
                    disabled={currentPage === adjustedTotalPages} // Desabilita se estiver na última página
                >
                    {'>'}
                </button>

            }
        </div>
    );
}