@Service
public class ImportarConsignadoService {

    private final ConsignadoService consignadoService;
    private final LeitorExcel leitorExcel;

    public ImportarConsignadoService(ConsignadoService consignadoService, LeitorExcel leitorExcel) {
        this.consignadoService = consignadoService;
        this.leitorExcel = leitorExcel;
    }

    public List<ConsignadoDTO> importar(MultipartFile arquivo, Long idCompetencia) {
        try (InputStream inputStream = arquivo.getInputStream()) {

            List<ConsignadoDTO> dados = leitorExcel.lerArquivo(inputStream, idCompetencia);

            List<Consignado> consignados = dados.stream()
                .map(dto -> new Consignado(
                    dto.contrato(),
                    dto.nome(),
                    dto.cpf(),
                    dto.matricula(),
                    dto.prazoTotal(),
                    dto.numeroPrestacao(),
                    dto.valorPrestacao(),
                    dto.competenciaId()
                ))
                .toList();

            consignadoService.salvarTodos(consignados);

            System.out.println("Total de consignados importados: " + consignados.size());
            return dados;

        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler o arquivo: " + e.getMessage(), e);
        }
    }
}