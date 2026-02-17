package co.edu.sena.r2dbc.config;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.MySqlDialect;

@Configuration
public class R2dbcConfig {

    @Bean
    public R2dbcCustomConversions r2dbcCustomConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new UuidToByteArrayConverter());
        converters.add(new ByteArrayToUuidConverter());
        return R2dbcCustomConversions.of(MySqlDialect.INSTANCE, converters);
    }

    @WritingConverter
    public static class UuidToByteArrayConverter implements Converter<UUID, byte[]> {

        @Override
        public byte[] convert(UUID source) {
            ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
            bb.putLong(source.getMostSignificantBits());
            bb.putLong(source.getLeastSignificantBits());
            return bb.array();
        }
    }

    @ReadingConverter
    public static class ByteArrayToUuidConverter implements Converter<byte[], UUID> {

        @Override
        public UUID convert(byte[] source) {
            ByteBuffer bb = ByteBuffer.wrap(source);
            long firstLong = bb.getLong();
            long secondLong = bb.getLong();
            return new UUID(firstLong, secondLong);
        }
    }
}
